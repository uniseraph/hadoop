package org.apache.hadoop.yarn.server.nodemanager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.server.nodemanager.containermanager.container.Container;
import org.apache.hadoop.yarn.util.ConverterUtils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig.DockerClientConfigBuilder;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;;

public class NativeDockerExecutor extends ContainerExecutor {

	private static final Log LOG = LogFactory.getLog(NativeDockerExecutor.class);
	public static final String DOCKER_CONTAINER_EXECUTOR_SCRIPT = "docker_container_executor";
	public static final String DOCKER_CONTAINER_EXECUTOR_SESSION_SCRIPT = "docker_container_executor_session";

	private static final String DOCKER_HOST = "unix:///var/run/docker.sock";
	private DockerClient dockerClient;
	private Map<String, String> pid2containerId = new HashMap<>();

	@Override
	public void init() throws IOException {
		String auth = getConf().get(CommonConfigurationKeys.HADOOP_SECURITY_AUTHENTICATION);
		if (auth != null && !auth.equals("simple")) {
			throw new IllegalStateException("NativeDockerExecutor only works with simple authentication mode");
		}
		// String dockerExecutor =
		// getConf().get(YarnConfiguration.NM_DOCKER_CONTAINER_EXECUTOR_EXEC_NAME,
		// YarnConfiguration.NM_DEFAULT_DOCKER_CONTAINER_EXECUTOR_EXEC_NAME);
		// if (!new File(dockerExecutor).exists()) {
		// throw new IllegalStateException("Invalid docker exec path: " +
		// dockerExecutor);
		// }
		// ConfigBuilder cb = new ConfigBuilder().withDockerUrl(DOCKER_HOST);
		// dockerClient = new DefaultDockerClient(cb.build());

		DockerClientConfigBuilder  builder= DockerClientConfig.createDefaultConfigBuilder()
				.withDockerTlsVerify(false)
				.withDockerCertPath("")
				.withDockerHost(DOCKER_HOST);
		
				
		dockerClient = DockerClientBuilder.getInstance(builder).build();

	}

	@Override
	public void startLocalizer(Path nmPrivateContainerTokens, InetSocketAddress nmAddr, String user, String appId,
			String locId, LocalDirsHandlerService dirsHandler) throws IOException, InterruptedException {

	}

	@Override
	public int launchContainer(Container container, Path nmPrivateContainerScriptPath, Path nmPrivateTokensPath,
			String user, String appId, Path containerWorkDir, List<String> localDirs, List<String> logDirs)
			throws IOException {

		String containerImageName = container.getLaunchContext().getEnvironment()
				.get(YarnConfiguration.NM_DOCKER_CONTAINER_EXECUTOR_IMAGE_NAME);
		if (LOG.isDebugEnabled()) {
			LOG.debug("containerImageName from launchContext: " + containerImageName);
		}
		Preconditions.checkArgument(!Strings.isNullOrEmpty(containerImageName), "Container image must not be null");
		containerImageName = containerImageName.replaceAll("['\"]", "");

		// Precoenditions.checkArgument(saneDockerImage(containerImageName),
		// "Image: " + containerImageName + " is not a proper docker image");

		String net = container.getLaunchContext().getEnvironment()
				.get(YarnConfiguration.NM_DOCKER_CONTAINER_EXECUTOR_NET);
		if (Strings.isNullOrEmpty(net)) {
			net = YarnConfiguration.NM_DEFAULT_DOCKER_CONTAINER_EXECUTOR_NET;
		}

		ContainerId containerId = container.getContainerId();

		// create container dirs on all disks
		String containerIdStr = ConverterUtils.toString(containerId);
		String appIdStr = ConverterUtils.toString(containerId.getApplicationAttemptId().getApplicationId());

	
			
			
			CreateContainerResponse  ccr = dockerClient.createContainerCmd(containerImageName)
				.withContainerIDFile("")
				.withName(containerIdStr)
				.withNetworkMode(net)
				.withRestartPolicy(RestartPolicy.alwaysRestart())
				.exec();

//			final HostConfig hostConfig = HostConfig.builder().restartPolicy(RestartPolicy.always()).networkMode(net)
//					.build();
//			final ContainerConfig containerConfig = ContainerConfig.builder().hostConfig(hostConfig)
//					.image(containerImageName).build();
//
//			final ContainerCreation creation = dockerClient.createContainer(containerConfig, containerIdStr);
			
			if (LOG.isInfoEnabled()){
				LOG.info("create container succes , containerid is "+ccr.getId());
			}
			
			dockerClient.startContainerCmd(ccr.getId()).exec();

			pid2containerId.put(
					dockerClient.inspectContainerCmd(ccr.getId()).exec().getState().getPid().toString(),
					ccr.getId());

			return 0;
		
	}

	@Override
	public boolean signalContainer(String user, String pid, Signal signal) throws IOException {
		if (pid2containerId.containsKey(pid) == false) {
			return false;
		}
		//TODO
		try {
			dockerClient.killContainerCmd(pid2containerId.get(pid)).exec();
		} catch (NotFoundException e) {
			LOG.info(e.getMessage(),e);
			return false;
		}
		return true;
	}

	@Override
	public void deleteAsUser(String user, Path subDir, Path... basedirs) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isContainerProcessAlive(String user, String pid) throws IOException {

		if (pid2containerId.containsKey(pid) == false) {
			return false;
		}
		try {
			return dockerClient.inspectContainerCmd(pid2containerId.get(pid)).exec().getState().getRunning();
		} catch (NotFoundException e) {
			LOG.info(e.getMessage(),e);
			return false;
		}
	}

}
