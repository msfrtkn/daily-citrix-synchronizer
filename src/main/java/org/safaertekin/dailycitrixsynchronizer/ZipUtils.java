package org.safaertekin.dailycitrixsynchronizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils
{
    private String m_sourceFolder = "";
    private String m_outputZip = "";

    private List<String> fileList;


    public ZipUtils(String sourceFolder)
    {
        fileList = new ArrayList<String>();
        m_sourceFolder = sourceFolder;
    }

    public void zipIt()
    {
        byte[] buffer = new byte[1024];
        String source = "";
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try
        {
            try
            {
                source = m_sourceFolder.substring(m_sourceFolder.lastIndexOf("\\") + 1);
            }
            catch (Exception e)
            {
                source = m_sourceFolder;
            }
            m_outputZip = source + ".zip";
            fos = new FileOutputStream(m_outputZip);
            zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + m_outputZip);
            FileInputStream in = null;

            for (String file : this.fileList)
            {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try
                {
                    in = new FileInputStream(m_sourceFolder + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0)
                    {
                        zos.write(buffer, 0, len);
                    }
                }
                finally
                {
                    in.close();
                }
            }

            zos.closeEntry();
            System.out.println("Folder successfully compressed");

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                zos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node)
    {

        // add file only
        if (node.isFile())
        {
            fileList.add(generateZipEntry(node.toString()));

        }

        if (node.isDirectory())
        {
            String[] subNote = node.list();
            for (String filename : subNote)
            {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file)
    {
        return file.substring(m_sourceFolder.length() + 1, file.length());
    }
}    