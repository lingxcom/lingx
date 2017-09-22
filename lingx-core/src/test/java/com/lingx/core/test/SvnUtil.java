package com.lingx.core.test;

import java.io.File;

public class SvnUtil {

	static final String SVN_NAME = ".svn";

	public static void main(String args[]) {
		System.out.println("开始处理");
		String path = "F:/java/yz-workspace-oav1/yangzi";
		File file = new File(path);

		deleteSvn(file);
		System.out.println("处理结束");
	}

	public static void deleteSvn(File file) {
		// System.out.println(file.getName());
		if (file.isDirectory())
			if (SVN_NAME.equalsIgnoreCase(file.getName())) {
				deleteDir(file);
			} else {
				File fl[] = file.listFiles();
				for (File f : fl) {
					if (f.isDirectory())
						deleteSvn(f);
				}
			}
	}

	static void deleteDir(File file) {
		if (file.isDirectory()) {
			File fl[] = file.listFiles();
			for (File f : fl) {
				deleteDir(f);
			}
			file.delete();
		} else {
			file.delete();
		}
	}
}
