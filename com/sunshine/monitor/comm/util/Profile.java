package com.sunshine.monitor.comm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
 
public class Profile extends HashMap
{
 private String filename;
 
   Profile(String filename)
    throws FileNotFoundException, IOException
  {
     this.filename = null;
     load(filename);
   }
 
   Profile()
  {
    this.filename = null;
  }

  public void load(String filename)
    throws FileNotFoundException, IOException
   {
     this.filename = filename;
     InputStream inputstream = getClass().getResourceAsStream(filename);
     BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
     String line = null;
     Map group = null;
     while (true)
     {
       line = br.readLine();
       if (line == null)
         break;
       String groupName = getGroup(line);
      if (groupName != null)
       {
         group = new HashMap();
         put(groupName, group); continue;
      }
 
       Entry entry = getEntry(line);
       if ((group != null) && (entry != null))
        group.put(entry.name, entry.value);
     }
   }
 
   public void saveAs(String filename)
     throws IOException
   {
     this.filename = filename;
     saveProfile();
   }
 
   public void saveProfile()
     throws IOException
   {
     if (this.filename == null)
       return;
     BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename));
     for (Iterator i = entrySet().iterator(); i.hasNext(); writer.write("\r\n"))
     {
      Map.Entry group = (Map.Entry)i.next();
       writer.write("[" + group.getKey() + "]\r\n");
       Map map = (Map)group.getValue();
      Map.Entry entry;
       for (Iterator j = map.entrySet().iterator(); j.hasNext(); writer.write(entry.getKey() + "=" + entry.getValue() + "\r\n")) {
         entry = (Map.Entry)j.next();
       }
     }
 
     writer.close();
   }
 
   private String getGroup(String line)
   {
     int start = line.indexOf('[');
     int end = line.indexOf(']');
     return ((start < 0 ? 1 : 0) | (end <= start ? 1 : 0)) != 0 ? null : line.substring(start + 1, end).trim();
   }
 
   public String getValue(String strGroup, String strItem)
   {
     Map map = (Map)get(strGroup);
     String strTempValue = (String)map.get(strItem);
    return strTempValue;
   }
 
   private Entry getEntry(String line)
   {
     int index = line.indexOf('=');
     return index >= 0 ? new Entry(line.substring(0, index).trim(), line.substring(index + 1)) : null;
   }

   public String getFilename()
   {
     return this.filename;
   }
 
   public void setFilename(String filename)
   {
    this.filename = filename;
   }
 
   public static void main(String[] args)
   {
     try
     {
       Profile profile = new Profile("/rbsp_setup.ini");
       System.out.println(profile.getValue("base", "ip"));
     }
     catch (Exception localException)
     {
     }
   }
 
   private class Entry
   {
     public String name;
     public String value;
 
     public Entry(String name, String value)
     {
       this.name = name;
       this.value = value;
     }
   }
 }