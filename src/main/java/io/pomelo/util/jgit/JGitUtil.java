package io.pomelo.util.jgit;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * {@link https://github.com/centic9/jgit-cookbook}
 * 
 * @author PomeloMan
 */
public class JGitUtil {

	private final static Log logger = LogFactory.getLog(JGitUtil.class);
	private final static boolean debug = logger.isDebugEnabled();

	public static Git clone(String remoteUrl, File localRepoDir)
			throws InvalidRemoteException, TransportException, GitAPIException {
		try (Git git = Git.cloneRepository().setURI(remoteUrl).setDirectory(localRepoDir).call()) {
			return git;
		}
	}

	public static void commit(File localRepoDir, String message) throws IOException, GitAPIException {
		commit(localRepoDir, message, null, null, false);
	}

	public static void commit(File localRepoDir, String message, String username, String password)
			throws IOException, GitAPIException {
		commit(localRepoDir, message, username, password, false);
	}

	public static void commit(File localRepoDir, String message, String username, String password, boolean force)
			throws IOException, GitAPIException {
		try (Git git = Git.open(localRepoDir)) {
			List<DiffEntry> diffs = git.diff().call();
			if (diffs.isEmpty()) {
				if (debug) {
					logger.debug("No diff changes");
				}
				return;
			}
			// Stage all files in the repo including new files
			DirCache cache = git.add().addFilepattern(".").call();
			if (debug) {
				logger.debug(cache.getEntryCount() + " entries available");
			}
			RevCommit revCommit = git.commit().setAll(true).setMessage(message).call();
			if (debug) {
				logger.debug("Commit ID: " + revCommit.getId().getName());
			}
			if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
				// push
				Iterator<PushResult> iter = git.push().setForce(force)
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)).call()
						.iterator();
				while (iter.hasNext()) {
					PushResult pushResult = iter.next();
					Collection<RemoteRefUpdate> updates = pushResult.getRemoteUpdates();
					for (RemoteRefUpdate remoteRefUpdate : updates) {
						if (debug) {
							logger.debug("Remote ref update status: " + remoteRefUpdate.getStatus().name());
						}
					}
				}
			}
		}
	}

	/**
	 * <p>
	 * pull -> fetch + merge
	 * </p>
	 * 
	 * @param localRepoDir
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public static boolean pull(File localRepoDir) throws IOException, GitAPIException {
		try (Git git = Git.open(localRepoDir)) {
			PullResult result = git.pull().call();
			return result.isSuccessful();
		}
	}

}
