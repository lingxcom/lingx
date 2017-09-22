package com.lingx.support.workflow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
/*
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;*/
import org.apache.commons.io.FilenameUtils;

public class ProcessDefinitionGeneratorEx {
	/* private RepositoryService repositoryService;  
	  private RuntimeService runtimeService;  
	   
	    public ProcessDefinitionGeneratorEx(ProcessEngine processEngine) {  
	    	this.repositoryService=processEngine.getRepositoryService();
	    	this.runtimeService=processEngine.getRuntimeService();
	    }  
	   
	    public RepositoryService getRepositoryService() {  
	        return repositoryService;  
	    }  
	   
	    public RuntimeService getRuntimeService() {  
	        return runtimeService;  
	    }  
	   
	    private static Color HIGHLIGHT_COLOR = Color.RED;  
	    private static Stroke THICK_TASK_BORDER_STROKE = new BasicStroke(1.0f);  
	   
	    public InputStream generateDiagramWithHighLight(String processInstanceId) throws IOException {  
	        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()  
	                .processInstanceId(processInstanceId).singleResult();  
	        String processDefinitionId = processInstance.getProcessDefinitionId();  
	   
	        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)  
	                .getDeployedProcessDefinition(processDefinitionId);  
	        String diagramResourceName = definition.getDiagramResourceName();  
	        String deploymentId = definition.getDeploymentId();  
	        InputStream originDiagram = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);  
	        BufferedImage image = ImageIO.read(originDiagram);  
	   
	        List<String> activeActivityIds = ActivityUtil.getActiveActivityIds(runtimeService, processInstanceId);  
	        List<ActivityImpl> definitionActivities = ActivityUtil.getFlatAllActivities(definition);  
	        for (ActivityImpl activity : definitionActivities) {  
	            if (activeActivityIds.contains(activity.getId())) {  
	            	
	                decorate(image, //  
	                        activity.getX(), activity.getY(), activity.getWidth(), activity.getHeight());  
	            }  
	        }  
	   
	        ByteArrayOutputStream out = new ByteArrayOutputStream();  
	        String formatName = getDiagramExtension(diagramResourceName);  
	        ImageIO.write(image, formatName, out);  
	        return new ByteArrayInputStream(out.toByteArray());  
	   
	    }  */
	      
	    /**  
	    @Test 
	    public void testDecorete() throws FileNotFoundException, IOException { 
	        String diagramResourceName = "C:/Users/Winseliu/Desktop/03.jpg"; 
	        BufferedImage image = ImageIO.read(new FileInputStream(diagramResourceName)); 
	        decorate(image, 100, 100, 100, 100); 
	        FileOutputStream out = new FileOutputStream("C:/Users/Winseliu/Desktop/03-hh.jpg"); 
	        try { 
	            ImageIO.write(image, getDiagramExtension(diagramResourceName), out); 
	        } finally { 
	            IoUtil.closeSilently(out); 
	        } 
	    } 
	    //*/  
	   
	   /* private static void decorate(BufferedImage image, int x, int y, int width, int height) {  
	        Graphics2D g = image.createGraphics();  
	        try {  
	            drawHighLight(x, y, width, height, g);  
	        } finally {  
	            g.dispose();  
	        }  
	    }  */
	   
	    private static String getDiagramExtension(String diagramResourceName) {  
//	      return diagramResourceName.substring(diagramResourceName.lastIndexOf(".") + 1);  
	        return FilenameUtils.getExtension(diagramResourceName);  
	    }  
	   /*
	    private static void drawHighLight(int x, int y, int width, int height, Graphics2D g) {  
	        Paint originalPaint = g.getPaint();  
	        Stroke originalStroke = g.getStroke();  
	   
	        g.setPaint(HIGHLIGHT_COLOR);  
	        g.setStroke(THICK_TASK_BORDER_STROKE);  
	   
	        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 10, 10);  
	        g.draw(rect);  
	   
	        g.setPaint(originalPaint);  
	        g.setStroke(originalStroke);  
	    }  */
	   
}
