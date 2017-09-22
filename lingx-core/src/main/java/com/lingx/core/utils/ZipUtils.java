package com.lingx.core.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ZipUtils {
	public static void main(String args[]){
		String path="F:\\java\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\alingx\\upload\\synchro\\tmp_upload.zip";
		String dir="F:\\java\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\alingx\\upload\\synchro\\tmp_upload";
		try {
			compressDirectory2Zip(dir,path);
			readZipFile(path,"update.xml");
			//decompressZip(path,dir);
			//Zip.unZipToFolder(path, dir);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//decompressZip(path,dir);
		/*String zipFile="D:\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\bee\\ScriptFile\\tuser.zip";
		String path="D:\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\bee\\ScriptFile\\tuser";
		compressDirectory2Zip(path,zipFile);*/
		/*try {
			System.out.println(readZipFile("F:/java/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/bee/upload/synchro/2013/12/27/1388132706261.zip","update.xml"));
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
	}
	public static void compressDirectory2Zip2(){}
	public static String readZipFile2(String zipfile,String filename){
		StringBuilder sb=new StringBuilder();
		try {
			java.util.zip.ZipFile zipFile=new java.util.zip.ZipFile(zipfile);
			System.out.println(zipFile.getEntry(filename));
	        InputStreamReader isr=new InputStreamReader(zipFile.getInputStream(zipFile.getEntry(filename)),"UTF-8");
	        BufferedReader br = new BufferedReader(isr);  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	        	sb.append(line).append("\r\n");
	           // System.out.println(line);  
	        }  
	        br.close();  
	        zipFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static String readZipFile(String zipfile , String filename) throws Exception {  
		StringBuilder sb=new StringBuilder();
		
        ZipFile zf = new ZipFile(zipfile);  
        if( zf.getEntry(filename)==null){
        	filename="/"+filename;
        }
        InputStreamReader isr=new InputStreamReader(zf.getInputStream(zf.getEntry(filename)),"UTF-8");
        BufferedReader br = new BufferedReader(isr);  
        String line;  
        while ((line = br.readLine()) != null) {  
        	sb.append(line).append("\r\n");
           // System.out.println(line);  
        }  
        br.close();  
        zf.close();
        return sb.toString();
    }  

	public static void compressDirectory2Zip(String file, String zipFilePath){
		 	try {
				Zip zip = new Zip(zipFilePath);
				zip.createZipOut();
				zip.packToolFiles(file, "");
				zip.closeZipOut();
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	}
	/**
	 * 
	 * 把文件压缩成zip格式
	 * 
	 * @param files
	 *            需要压缩的文件
	 * 
	 * @param zipFilePath
	 *            压缩后的zip文件路径 ,如"D:/test/aa.zip";
	 */

	public static void compressFiles2Zip(File[] files, String zipFilePath) {

		if (files != null && files.length > 0) {

			if (isEndsWithZip(zipFilePath)) {

				ZipArchiveOutputStream zaos = null;

				try {

					File zipFile = new File(zipFilePath);

					zaos = new ZipArchiveOutputStream(zipFile);

					// Use Zip64 extensions for all entries where they are
					// required

					zaos.setUseZip64(Zip64Mode.AsNeeded);

					// 将每个文件用ZipArchiveEntry封装

					// 再用ZipArchiveOutputStream写到压缩文件中

					for (File file : files) {

						if (file != null) {

							ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(
									file, file.getName());

							zaos.putArchiveEntry(zipArchiveEntry);

							InputStream is = null;

							try {

								is = new FileInputStream(file);

								byte[] buffer = new byte[1024 * 5];

								int len = -1;

								while ((len = is.read(buffer)) != -1) {

									// 把缓冲区的字节写入到ZipArchiveEntry

									zaos.write(buffer, 0, len);

								}

								// Writes all necessary data for this entry.

								zaos.closeArchiveEntry();

							} catch (Exception e) {

								throw new RuntimeException(e);

							} finally {

								if (is != null)

									is.close();

							}

						}

					}

					zaos.finish();

				} catch (Exception e) {

					throw new RuntimeException(e);

				} finally {

					try {

						if (zaos != null) {

							zaos.close();

						}

					} catch (IOException e) {

						throw new RuntimeException(e);

					}

				}

			}

		}

	}

	/**
	 * 
	 * 把zip文件解压到指定的文件夹
	 * 
	 * @param zipFilePath
	 *            zip文件路径, 如 "D:/test/aa.zip"
	 * 
	 * @param saveFileDir
	 *            解压后的文件存放路径, 如"D:/test/"
	 */

	public static void decompressZip(String zipFilePath, String saveFileDir) {
		try {
			Zip.unZipToFolder(zipFilePath, saveFileDir);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 判断文件名是否以.zip为后缀
	 * 
	 * @param fileName
	 *            需要判断的文件名
	 * 
	 * @return 是zip文件返回true,否则返回false
	 */

	public static boolean isEndsWithZip(String fileName) {

		boolean flag = false;

		if (fileName != null && !"".equals(fileName.trim())) {

			if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {

				flag = true;

			}

		}

		return flag;

	}


}
class Zip {
    private OutputStream out = null;
    private BufferedOutputStream bos = null;
    private ZipArchiveOutputStream zaos = null;
    private String zipFileName = null;

    /**
     * 把一个目录打包到ZIP文件中的某目录
     * @param dirpath  目录绝对地址
     * @param pathName ZIP中目录
     */
    public void packToolFiles(String dirpath, String pathName) throws FileNotFoundException, IOException {
        if (pathName!=null) {
            pathName = pathName + "/";
        }
        packToolFiles(zaos, dirpath, pathName);
    }

    /**
     * 把一个目录打包到一个指定的ZIP文件中
     * @param dirpath 目录绝对地址
     * @param pathName ZIP文件抽象地址
     */
    public void packToolFiles(ZipArchiveOutputStream zaos, String dirpath, String pathName)
            throws FileNotFoundException, IOException {
        
        File dir = new File(dirpath);
        // 返回此绝对路径下的文件
        File[] files = dir.listFiles();
        if (files == null || files.length < 1) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            // 判断此文件是否是一个文件夹
            if (files[i].isDirectory()) {
                packToolFiles(zaos, files[i].getAbsolutePath(), pathName + files[i].getName() + "/");
            } else {
            	InputStream inputStream=new FileInputStream(files[i].getAbsolutePath());
                zaos.putArchiveEntry(new ZipArchiveEntry(pathName + files[i].getName()));
                IOUtils.copy(inputStream, zaos);
                inputStream.close();
                zaos.closeArchiveEntry();
            }
        }
    }

    /**
     * 把一个ZIP文件解压到一个指定的目录中
     * 
     * @param zipfilename
     *            ZIP文件抽象地址
     * @param outputdir
     *            目录绝对地址
     */
    @SuppressWarnings("unchecked")
    public static void unZipToFolder(String zipfilename, String outputdir) throws IOException {
        File zipfile = new File(zipfilename);
        if (zipfile.exists()) {
            outputdir = outputdir + "/";
            FileUtils.forceMkdir(new File(outputdir));

            ZipFile zf = new ZipFile(zipfile, "UTF-8");
            Enumeration zipArchiveEntrys = zf.getEntries();
            while (zipArchiveEntrys.hasMoreElements()) {
                ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry) zipArchiveEntrys.nextElement();
                if (zipArchiveEntry.isDirectory()) {
                    FileUtils.forceMkdir(new File(outputdir + zipArchiveEntry.getName() + "/"));
                } else {
                	FileOutputStream fileOutputStream=FileUtils.openOutputStream(new File(outputdir
                            + zipArchiveEntry.getName()));
                	InputStream inputStream=zf.getInputStream(zipArchiveEntry);
                    IOUtils.copy(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                }
            }
            zf.close();
        } else {
            throw new IOException("指定的解压文件不存在：\t" + zipfilename);
        }
    }

    public Zip(String zipFileName) {
        this.zipFileName = zipFileName;
    }

    public void createZipOut() throws FileNotFoundException, IOException {
        File f = new File(zipFileName);
        out = new FileOutputStream(f);
        bos = new BufferedOutputStream(out);
        zaos = new ZipArchiveOutputStream(bos);
        zaos.setEncoding("UTF-8");
    }

    public void closeZipOut() throws Exception {
        zaos.flush();
        zaos.close();

        bos.flush();
        bos.close();

        out.flush();
        out.close();
    }
}
