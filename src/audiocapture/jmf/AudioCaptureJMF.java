/*
 *@author Paradox
 * Audio Capture using Java Media Framework
 */
package audiocapture.jmf;


import java.awt.*;
import java.net.*;
import java.io.File;
import java.util.Arrays;
import java.applet.Applet;
import javax.swing.JApplet;
import java.io.IOException;
import javax.media.CaptureDeviceInfo;
import javax.media.*;
import java.net.URL;
import java.util.*;
import javax.media.format.AudioFormat;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;

public class AudioCaptureJMF {
    /*
     * @param args
     */    
    public static void main(String[] args) throws NoDataSinkException, MalformedURLException {
        
        Vector<CaptureDeviceInfo> devices = new Vector<>();
        devices = CaptureDeviceManager.getDeviceList ( new AudioFormat(AudioFormat.LINEAR) );
        CaptureDeviceInfo di = null;
        MediaLocator mic = devices.elementAt(0).getLocator();
        
        System.out.println(devices.size());
        
        if (devices.size() > 0)
         di = (CaptureDeviceInfo)devices.elementAt(1);
        else
        // Exit if we can't find a device that does linear
        System.exit(-1); 
        
            try{
            Player p1 = Manager.createPlayer(di.getLocator()); 
            p1.start();
            }
            catch (IOException | NoPlayerException e) { 
            }
            
            DataSource audioInputSource = null, processedAudioSource= null;
            
            try{
            audioInputSource = Manager.createDataSource(mic);
            }
            catch(IOException | NoDataSourceException e){
            System.out.println("(error)-- no mic");
            }
            
        Processor p;
        p = null;
        DataSink sink = null;
        Format[] formats = new Format[1];
        formats[0] = new AudioFormat(AudioFormat.GSM_RTP,8000,16,1);
        MediaLocator dest = new MediaLocator("live.wav");
        URL url = null;
        MediaLocator file;
        
            try{
                url = new URL("file", null, "clap.wav");
                file = new MediaLocator(url);
            }
            catch(){
            }
            
            try
            {
                p = Manager.createRealizedProcessor(new ProcessorModel(audioInputSource,formats,new FileTypeDescriptor(FileTypeDescriptor.WAVE)));
                processedAudioSource = p.getDataOutput();
                System.out.println(processedAudioSource.getContentType());
                
                sink = Manager.createDataSink(p.getDataOutput(), dest);
                p.start();  
                sink.open();
                sink.start();
            
            }  
            catch(IOException | NoProcessorException | CannotRealizeException e)
            {
                System.out.println("(error)-- no storage");
            }
               
        /*
        DataSink sink;
        MediaLocator dest = new MediaLocator("hello.wav");
        try {
        sink = Manager.createDataSink(p.getDataOutput(), dest);
        sink.open();
        sink.start();
        }
        catch (Exception e) {
        e.printStackTrace();
        System.out.println("()--Error");
        }
         */  
    }   
    
}
