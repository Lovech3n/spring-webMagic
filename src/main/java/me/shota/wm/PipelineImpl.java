package me.shota.wm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;

public class PipelineImpl extends FilePipeline implements Pipeline {
	
	public PipelineImpl(String path) {
		setPath(path);
	}
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		String filepath = this.path;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
			List<String> list = (List<String>) entry.getValue();
			for(String str : list) {
				try {
					HttpGet httpget = new HttpGet(str);
					HttpResponse response = httpclient.execute(httpget);
	                HttpEntity entity = response.getEntity();
	                InputStream in = entity.getContent();
	                
	                try {
	                	String ext = str.substring(str.lastIndexOf("."), str.length());
	                	File file = new File(filepath+File.separator+new Date().getTime()+ext);
                        FileOutputStream fout = new FileOutputStream(file);
                        int l = -1;
                        byte[] tmp = new byte[1024];
                        while ((l = in.read(tmp)) != -1) {
                            fout.write(tmp,0,l);
                        }
                        fout.flush();
                        fout.close();
                    } finally {

                        in.close();
                    }
	                
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
        }
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
