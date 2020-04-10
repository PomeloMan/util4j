package io.pomelo.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

import io.pomelo.util.jgit.JGitUtil;

public class JGitUtilTest {

	private String url = "https://github.com/PomeloMan/test.git";
	private File dir = new File("D:\\_jgit");

	@Test
	public void cloneRemoteRepo() throws Exception {
		JGitUtil.clone(url, dir);
	}

	@Test
	public void commit() throws IOException, GitAPIException {
		JGitUtil.commit(dir, "test");
	}

	@Test
	public void pull() throws IOException, GitAPIException {
		System.out.println(JGitUtil.pull(dir));
	}

}
