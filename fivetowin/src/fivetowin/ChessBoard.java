package fivetowin;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
public class ChessBoard extends JPanel implements MouseListener{
	public static final int MARGIN=30;//边距
	   public static final int GRID_SPAN=35;//网格间距
	   public static final int ROWS=15;//棋盘行数
	   public static final int COLS=15;//棋盘列数
	   
	   Point[] chessList=new Point[(ROWS+1)*(COLS+1)];//初始每个数组元素为null
	   boolean isBlack=true;//默认开始是黑棋先
	   boolean gameOver=false;//游戏是否结束
	   int chessCount;//当前棋盘棋子的个数
	   int xIndex,yIndex;//当前刚下棋子的索引
	   
	   Image img;
	   Image shadows;
	   Color colortemp;
	   public ChessBoard(){
		  
		   setBackground(Color.orange);//设置背景色为橘黄色
		   img=Toolkit.getDefaultToolkit().getImage("board.jpg");
		   shadows=Toolkit.getDefaultToolkit().getImage("shadows.jpg");
		   addMouseListener(this);
		   addMouseMotionListener(new MouseMotionListener(){
			   public void mouseDragged(MouseEvent e){
				   
			   }
			   
			   public void mouseMoved(MouseEvent e){
			     int x1=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
			     //将鼠标点击的坐标位置转成网格索引
			     int y1=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
			     //游戏已经结束不能下
			     //落在棋盘外不能下
			     //x，y位置已经有棋子存在，不能下
			     if(x1<0||x1>ROWS||y1<0||y1>COLS||gameOver||findChess(x1,y1))
			    	 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			     //设置成默认状态
			     else setCursor(new Cursor(Cursor.HAND_CURSOR));
			     
			   }
		   });
	   } 
	   
	  

	//绘制
	   public void paintComponent(Graphics g){
		 
		   super.paintComponent(g);//画棋盘
		 
		   int imgWidth= img.getWidth(this);
		   int imgHeight=img.getHeight(this);//获得图片的宽度与高度
		   int FWidth=getWidth();
		   int FHeight=getHeight();//获得窗口的宽度与高度
		   int x=(FWidth-imgWidth)/2;
		   int y=(FHeight-imgHeight)/2;
		   g.drawImage(img, x, y, null);
		
		   
		   for(int i=0;i<=ROWS;i++){//画横线
			   g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);
		   }
		   for(int i=0;i<=COLS;i++){//画竖线
			   g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, MARGIN+ROWS*GRID_SPAN);
			   
		   }
		   
		   //画棋子
		   for(int i=0;i<chessCount;i++){
			   //网格交叉点x，y坐标
			   int xPos=chessList[i].getX()*GRID_SPAN+MARGIN;
			   int yPos=chessList[i].getY()*GRID_SPAN+MARGIN;
			   g.setColor(chessList[i].getColor());//设置颜色
			  // g.fillOval(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2,
					           //Point.DIAMETER, Point.DIAMETER);
			   //g.drawImage(shadows, xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER, null);
			   colortemp=chessList[i].getColor();
			   if(colortemp==Color.black){
				   RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 20, new float[]{0f, 1f}
	               , new Color[]{Color.WHITE, Color.BLACK});
	               ((Graphics2D) g).setPaint(paint);
	               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

			   }
			   else if(colortemp==Color.white){
				   RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 70, new float[]{0f, 1f}
	               , new Color[]{Color.WHITE, Color.BLACK});
	               ((Graphics2D) g).setPaint(paint);
	               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

			   }
			 
			   Ellipse2D e = new Ellipse2D.Float(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, 34, 35);
			   ((Graphics2D) g).fill(e);
		       //标记最后一个棋子的红矩形框
			   
			   if(i==chessCount-1){//如果是最后一个棋子
				   g.setColor(Color.red);
				   g.drawRect(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2,
					           34, 35);
			   }
		   }
	   }

	   public void mousePressed(MouseEvent e){//鼠标在组件上按下时调用
		   
		   //游戏结束时，不再能下
		   if(gameOver) return;
		   
		   String colorName=isBlack?"黑棋":"白棋";
		   if(!isBlack)
		   {
			  
				   artInt();
				   //artInt2();
				   
		   }
		   
		   else
		   //将鼠标点击的坐标位置转换成网格索引
		   {xIndex=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
		   yIndex=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
		   
		   //落在棋盘外不能下
		   if(xIndex<0||xIndex>ROWS||yIndex<0||yIndex>COLS)
			   return;
		   
		   //如果x，y位置已经有棋子存在，不能下
		   if(findChess(xIndex,yIndex))return;
		   
		   //可以进行时的处理,改！！！！！！
		   
		   Point ch=new Point(xIndex,yIndex,Color.black);
		   chessList[chessCount++]=ch;
		    repaint();//通知系统重新绘制
		   }
		  
		   int a1,b1;
		   a1=chessList[chessCount-1].getX();
		   b1=chessList[chessCount-1].getY();
		   //如果胜出则给出提示信息，不能继续下棋
		   if(isBlack)
		   {if(isWin(xIndex,yIndex)){
			   String msg=String.format("恭喜，%s赢了！", colorName);
			   JOptionPane.showMessageDialog(this, msg);
			   gameOver=true;
		   }}
		   else
		   {if(isWin(a1,b1)){
			   String msg=String.format("恭喜，%s赢了！", colorName);
			   JOptionPane.showMessageDialog(this, msg);
			   gameOver=true;
		   }}
		   isBlack=!isBlack;
		 }
	   //覆盖mouseListener的方法
	   public void mouseClicked(MouseEvent e){
		   //鼠标按键在组件上单击时调用
	   }
	   
	   public void mouseEntered(MouseEvent e){
		   //鼠标进入到组件上时调用
	   }
	   public void mouseExited(MouseEvent e){
		   //鼠标离开组件时调用
	   }
	   public void mouseReleased(MouseEvent e){
		   //鼠标按钮在组件上释放时调用
	   }
	   //在棋子数组中查找是否有索引为x，y的棋子存在
	   private boolean findChess(int x,int y){
		   for(Point c:chessList){
			   if(c!=null&&c.getX()==x&&c.getY()==y)
				   return true;
		   }
		   return false;
	   }
	   
	   private int returnChess(int x,int y){
		   int i=0;
		   for( i=0;i<chessCount;i++){
			   if(chessList[i]!=null&&chessList[i].getX()==x&&chessList[i].getY()==y)
				   break;
		   }
		   return i;
	   }
	   
	   private boolean isWin(int xIndex1,int yIndex1){
		   int continueCount=1;//连续棋子的个数
		  
		   //横向向西寻找
		   for(int x=xIndex1-1;x>=0;x--){
			   Color c=isBlack?Color.black:Color.white;
			   if(getChess(x,yIndex1,c)!=null){
				   continueCount++;
			   }else
				   break;
		   }
	      //横向向东寻找
	       for(int x=xIndex1+1;x<=COLS;x++){
		      Color c=isBlack?Color.black:Color.white;
		      if(getChess(x,yIndex1,c)!=null){
			     continueCount++;
		      }else
			     break;
	       }
	       if(continueCount>=5){
		         return true;
	       }else 
		   continueCount=1;
	       
	       //继续另一种搜索纵向
	       //向上搜索
	       for(int y=yIndex1-1;y>=0;y--){
	    	   Color c=isBlack?Color.black:Color.white;
	    	   if(getChess(xIndex1,y,c)!=null){
	    		   continueCount++;
	    	   }else
	    		   break;
	       }
	       //纵向向下寻找
	       for(int y=yIndex1+1;y<=ROWS;y++){
	    	   Color c=isBlack?Color.black:Color.white;
	    	   if(getChess(xIndex1,y,c)!=null)
	    	       continueCount++;
	           else
	    	      break;
	       
	       }
	       if(continueCount>=5)
	    	   return true;
	       else
	    	   continueCount=1;
	       
	       
	       //继续另一种情况的搜索：斜向
	       //东北寻找
	       for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
	    	   Color c=isBlack?Color.black:Color.white;
	    	   if(getChess(x,y,c)!=null){
	    		   continueCount++;
	    	   }
	    	   else break;
	       }
	       //西南寻找
	       for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
	    	   Color c=isBlack?Color.black:Color.white;
	    	   if(getChess(x,y,c)!=null){
	    		   continueCount++;
	    	   }
	    	   else break;
	       }
	       if(continueCount>=5)
	    	   return true;
	       else continueCount=1;
	       
	       
	       //继续另一种情况的搜索：斜向
	       //西北寻找
	       for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
	    	   Color c=isBlack?Color.black:Color.white;
	    	   if(getChess(x,y,c)!=null)
	    		   continueCount++;
	    	   else break;
	       }
	       //东南寻找
	       for(int x=xIndex1+1,y=yIndex1+1;x<=COLS&&y<=ROWS;x++,y++){
	    	   Color c=isBlack?Color.black:Color.white;
	    	   if(getChess(x,y,c)!=null)
	    		   continueCount++;
	    	   else break;
	       }
	       
	       if(continueCount>=5)
	    	   return true;
	       else continueCount=1;
	       
	       return false;
	     }
	   
	   
	   private Point getChess(int xIndex,int yIndex,Color color){
		   for(Point p:chessList){
			   if(p!=null&&p.getX()==xIndex&&p.getY()==yIndex
					   &&p.getColor()==color)
				   return p;
		   }
		   return null;
	   }
	   
	   
	   public void restartGame(){
		   //清除棋子
		   for(int i=0;i<chessList.length;i++){
			   chessList[i]=null;
		   }
		   //恢复游戏相关的变量值
		   isBlack=true;
		   gameOver=false; //游戏是否结束
		   chessCount =0; //当前棋盘棋子个数
		   repaint();
	   }
	   
	   //悔棋
	   public void goback(){
		   if(chessCount==0)
			   return ;
		   chessList[chessCount-1]=null;
		   chessCount--;
		   if(chessCount>0){
			   xIndex=chessList[chessCount-1].getX();
			   yIndex=chessList[chessCount-1].getY();
		   }
		   isBlack=!isBlack;
		   repaint();
	   }
	   
	   //矩形Dimension

	   public Dimension getPreferredSize(){
		   return new Dimension(MARGIN*2+GRID_SPAN*COLS,MARGIN*2
		                        +GRID_SPAN*ROWS);
	   }
	   
	   public void artInt()
	   {   int xIndex2,yIndex2,xIndex3,yIndex3;	   		  
			   xIndex2=chessList[chessCount-1].getX();
			   yIndex2=chessList[chessCount-1].getY();
			  
			 if(chessCount==1)
			   {   
				   if(jiezhexia(xIndex2,yIndex2))
				   {}
			   }
			 else
			  {   xIndex3=chessList[chessCount-2].getX();
		       yIndex3=chessList[chessCount-2].getY();
				 if(gotruefive()) 
				 {}
				 else
				 {if(isfour(xIndex2,yIndex2))
		        {}
			   else
		       {if(isgewu())
		       {}
		       else  
		       {  
		    	   {if(gotruefour())
		         {}
		       else
				{if(isthree(xIndex2,yIndex2))
				{}
				 else
					   {if(isgetwotwo())
		               {}
		                else 
		                {	if(isgethree())
		    				{}
		    				 else
		    				 {   if(iszhengfangxing())
		    				{}
		    				 else
		    				 {
		    					 if(istwo(xIndex2,yIndex2))
						  {}
						  else
					      {   if(gothree())
					           {}
					      else
					      {   if(gotwo())
					          {}
					          else
					    	  jiezhexia(xIndex3,yIndex3);
					    	  }   
					      
					      }
					   }
				   
			   }}
			}}}}}}}
		   
		   
		   
		   
	   }
	   
	  
	   private boolean istwo(int xIndex1,int yIndex1){
		   int continueCount1=1;//连续棋子的个数
		  
		   //横向向西寻找
		   for(int x=xIndex1-1;x>=0;x--){
			   Color c=Color.black;
			   if(getChess(x,yIndex1,c)!=null){
				   continueCount1++;
			   }else
			   { if(continueCount1>=2&&getChess(x,yIndex1,Color.white)==null&&getChess(x+3,yIndex1,Color.white)==null){
			    	Point ch1=new Point(x,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		   }
	   //横向向东寻找
	    for(int x=xIndex1+1;x<=COLS;x++){
		      Color c=Color.black;
		      if(getChess(x,yIndex1,c)!=null){
			     continueCount1++;
		      }else
		      { if(continueCount1>=2&&getChess(x,yIndex1,Color.white)==null&&getChess(x-3,yIndex1,Color.white)==null){
			    	Point ch1=new Point(x,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
	    }
	   
		   continueCount1=1;
	    
	    //继续另一种搜索纵向
	    //向上搜索
	    for(int y=yIndex1-1;y>=0;y--){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null){
	 		   continueCount1++;
	 	   }else
	 	  { if(continueCount1>=2&&getChess(xIndex1,y+3,Color.white)==null&&getChess(xIndex1,y,Color.white)==null){
		    	Point ch1=new Point(xIndex1,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;
		    }
	    	  break;} 
	    }
	    //纵向向下寻找
	    for(int y=yIndex1+1;y<=ROWS;y++){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null)
	 	       continueCount1++;
	        else
	        { if(continueCount1>=2&&getChess(xIndex1,y-3,Color.white)==null&&getChess(xIndex1,y,Color.white)==null){
		    	Point ch1=new Point(xIndex1,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;
		    }
	    	  break;} 
	    
	    }
	   
	 	   continueCount1=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //东北寻找
	    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount1++;
	 	   }
	 	   else  { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x-3,y+3,Color.white)==null){
		    	Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;
		    }
	    	  break;} 
	    }
	    //西南寻找
	    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount1++;
	 	   }
	 	   else  { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x+3,y-3,Color.white)==null){
		    	Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;
		    }
	    	  break;} 
	    }
	    continueCount1=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //西北寻找
	    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount1++;
	 	   else { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x+3,y+3,Color.white)==null){
		    	Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;
		    }
	    	  break;} 
	    }
	    //东南寻找
	    for(int x=xIndex1+1,y=yIndex1+1;x<=COLS&&y<=ROWS;x++,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount1++;
	 	   else  { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x-3,y-3,Color.white)==null){
		    	Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;
		    }
	    	  break;} 
	    }
	    continueCount1=1;	    
	    return false;
	  }
	  	   
	   private boolean isfour(int xIndex1,int yIndex1){
		   int continueCount3=1;//连续棋子的个数
		  
		   //横向向西寻找
		   for(int x=xIndex1-1;x>=0;x--){
			   Color c=Color.black;
			   if(getChess(x,yIndex1,c)!=null){
				   continueCount3++;
			   }else
			   { if(continueCount3>=4&&getChess(x,yIndex1,Color.white)==null){
			    	Point ch1=new Point(x,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		   }
	   //横向向东寻找
	    for(int x=xIndex1+1;x<=COLS;x++){
		      Color c=Color.black;
		      if(getChess(x,yIndex1,c)!=null){
			     continueCount3++;
		      }else
		      { if(continueCount3>=4&&(getChess(x,yIndex1,Color.white)==null||getChess(x-5,yIndex1,Color.white)==null))
		      {    if(getChess(x,yIndex1,Color.white)==null)
			    	{Point ch1=new Point(x,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
		      else
		      {Point ch1=new Point(x-5,yIndex1,Color.white);
			   chessList[chessCount++]=ch1;
			    repaint();
			    return true;}
			    }
		    	  break;} 
	    }
	   
		   continueCount3=1;
	    
	    //继续另一种搜索纵向
	    //向上搜索
	    for(int y=yIndex1-1;y>=0;y--){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null){
	 		   continueCount3++;
	 	   }else
	 	  { if(continueCount3>=4&&getChess(xIndex1,y,Color.white)==null){
		    	Point ch1=new Point(xIndex1,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;
		    }
	    	  break;} 
	    }
	    //纵向向下寻找
	    for(int y=yIndex1+1;y<=ROWS;y++){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null)
	 	       continueCount3++;
	        else
	        { if(continueCount3>=4&&(getChess(xIndex1,y,Color.white)==null||getChess(xIndex1,y-5,Color.white)==null)){
		    	if(getChess(xIndex1,y,Color.white)==null)
	        	{Point ch1=new Point(xIndex1,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	else
		    	{Point ch1=new Point(xIndex1,y-5,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    }
	    	  break;} 
	    
	    }
	   
	 	   continueCount3=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //东北寻找
	    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount3++;
	 	   }
	 	   else  { if(continueCount3>=4&&getChess(x,y,Color.white)==null){
		    	Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;
		    }
	    	  break;} 
	    }
	    //西南寻找
	    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount3++;
	 	   }
	 	   else  { if(continueCount3>=4&&(getChess(x,y,Color.white)==null||getChess(x+5,y-5,Color.white)==null))
	 	   {   if(getChess(x,y,Color.white)==null)
		    	{Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
	 	   else
	 	   {Point ch1=new Point(x+5,y-5,Color.white);
		   chessList[chessCount++]=ch1;
		    repaint();
		    return true;}
		    }
	    	  break;} 
	    }
	    continueCount3=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //西北寻找
	    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount3++;
	 	   else { if(continueCount3>=4&&getChess(x,y,Color.white)==null){
		    	Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;
		    }
	    	  break;} 
	    }
	    //东南寻找
	    for(int x=xIndex1+1,y=yIndex1+1;x<=COLS&&y<=ROWS;x++,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount3++;
	 	   else  { if(continueCount3>=4&&(getChess(x,y,Color.white)==null||getChess(x-5,y-5,Color.white)==null))
	 	   {
		    	if(getChess(x,y,Color.white)==null)
	 		   {Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	else
		    	{Point ch1=new Point(x-5,y-5,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    }
	    	  break;} 
	    }
	    continueCount3=1;	    
	    return false;
	  }
	    
	   private boolean jiezhexia(int xIndex1,int yIndex1)
	    {
	    	if(getChess(xIndex1+1,yIndex1,Color.white)==null&&getChess(xIndex1+1,yIndex1,Color.black)==null)
	    	{Point ch1=new Point(xIndex1+1,yIndex1,Color.white);
			   chessList[chessCount++]=ch1;
			    repaint();
			    return true;}
	    	else
	    	{
	    		if(getChess(xIndex1,yIndex1+1,Color.white)==null&&getChess(xIndex1,yIndex1+1,Color.black)==null)
	    		{Point ch1=new Point(xIndex1,yIndex1+1,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
	    		else
	    		{
	    		if(getChess(xIndex1+1,yIndex1+1,Color.white)==null&&getChess(xIndex1+1,yIndex1+1,Color.black)==null)
	    		{Point ch1=new Point(xIndex1+1,yIndex1+1,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
	    		else
	    		{
	    			if(getChess(xIndex1-1,yIndex1+1,Color.white)==null&&getChess(xIndex1-1,yIndex1+1,Color.black)==null)
	    			{Point ch1=new Point(xIndex1-1,yIndex1+1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
	    			else
	    			{
	    				if(getChess(xIndex1+1,yIndex1-1,Color.white)==null&&getChess(xIndex1+1,yIndex1-1,Color.black)==null)
		    			{Point ch1=new Point(xIndex1+1,yIndex1-1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}
	    				else
	    				{
	    					if(getChess(xIndex1,yIndex1-1,Color.white)==null&&getChess(xIndex1,yIndex1-1,Color.black)==null)
			    			{Point ch1=new Point(xIndex1,yIndex1-1,Color.white);
							   chessList[chessCount++]=ch1;
							    repaint();
							    return true;}
	    					else
	    					{
	    						if(getChess(xIndex1-1,yIndex1,Color.white)==null&&getChess(xIndex1-1,yIndex1,Color.black)==null)
				    			{Point ch1=new Point(xIndex1-1,yIndex1,Color.white);
								   chessList[chessCount++]=ch1;
								    repaint();
								    return true;}
	    						else
	    						{
	    							if(getChess(xIndex1-1,yIndex1-1,Color.white)==null&&getChess(xIndex1-1,yIndex1-1,Color.black)==null)
					    			{Point ch1=new Point(xIndex1-1,yIndex1-1,Color.white);
									   chessList[chessCount++]=ch1;
									    repaint();
									    return true;}
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}
	    	return false;
	    }
	}
	    
	   private boolean isthree(int xIndex1,int yIndex1){
			   int continueCount3=1;//连续棋子的个数
			  
			   //横向向西寻找
			   for(int x=xIndex1-1;x>=0;x--){
				   Color c=Color.black;
				   if(getChess(x,yIndex1,c)!=null){
					   continueCount3++;
				   }else
				   { if(continueCount3>=3&&getChess(x,yIndex1,Color.white)==null&&getChess(x+4,yIndex1,Color.white)==null)
				   {   
				    	Point ch1=new Point(x,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;
				    }
			    	  break;} 
			   }
		   //横向向东寻找
		    for(int x=xIndex1+1;x<=COLS;x++){
			      Color c=Color.black;
			      if(getChess(x,yIndex1,c)!=null){
				     continueCount3++;
			      }else
			      { if(continueCount3>=3&&getChess(x,yIndex1,Color.white)==null&&getChess(x-4,yIndex1,Color.white)==null)
			      {   Point ch1=new Point(x,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;
			     
				    }
			    	  break;} 
		    }
		   
			   continueCount3=1;
		    
		    //继续另一种搜索纵向
		    //向上搜索
		    for(int y=yIndex1-1;y>=0;y--){
		 	   Color c=Color.black;
		 	   if(getChess(xIndex1,y,c)!=null){
		 		   continueCount3++;
		 	   }else
		 	  { if(continueCount3>=3&&getChess(xIndex1,y,Color.white)==null&&getChess(xIndex1,y+4,Color.white)==null){
			    	Point ch1=new Point(xIndex1,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    //纵向向下寻找
		    for(int y=yIndex1+1;y<=ROWS;y++){
		 	   Color c=Color.black;
		 	   if(getChess(xIndex1,y,c)!=null)
		 	       continueCount3++;
		        else
		        { if(continueCount3>=3&&getChess(xIndex1,y,Color.white)==null&&getChess(xIndex1,y-4,Color.white)==null){
			    	Point ch1=new Point(xIndex1,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;			   
			    }
		    	  break;} 
		    
		    }
		   
		 	   continueCount3=1;
		    
		    
		    //继续另一种情况的搜索：斜向
		    //东北寻找
		    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
		 	   Color c=Color.black;
		 	   if(getChess(x,y,c)!=null){
		 		   continueCount3++;
		 	   }
		 	   else  { if(continueCount3>=3&&getChess(x,y,Color.white)==null&&getChess(x-4,y+4,Color.white)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    //西南寻找
		    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
		 	   Color c=Color.black;
		 	   if(getChess(x,y,c)!=null){
		 		   continueCount3++;
		 	   }
		 	   else  { if(continueCount3>=3&&getChess(x,y,Color.white)==null&&getChess(x+4,y-4,Color.white)==null)
		 	   {   Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
		 	   
			    }
		    	  break;} 
		    }
		    continueCount3=1;
		    
		    
		    //继续另一种情况的搜索：斜向
		    //西北寻找
		    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
		 	   Color c=Color.black;
		 	   if(getChess(x,y,c)!=null)
		 		   continueCount3++;
		 	   else { if(continueCount3>=3&&getChess(x,y,Color.white)==null&&getChess(x+4,y+4,Color.white)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    //东南寻找
		    for(int x=xIndex1+1,y=yIndex1+1;x<=COLS&&y<=ROWS;x++,y++){
		 	   Color c=Color.black;
		 	   if(getChess(x,y,c)!=null)
		 		   continueCount3++;
		 	   else  { if(continueCount3>=3&&getChess(x,y,Color.white)==null&&getChess(x-4,y-4,Color.white)==null)
		 	   {
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    	
			    }
		    	  break;} 
		    }
		    continueCount3=1;	    
		    return false;
		  }
	    
	   private boolean gothree(){
			   for(int j=0;j<chessCount;j++)
			   {
			if(chessList[j].getColor().equals(Color.white))	   
			   {int continueCount2=1;//连续棋子的个数
			   int xIndex1,yIndex1;
			   xIndex1=chessList[j].getX();
			   yIndex1=chessList[j].getY();
			   //横向向西寻找
			   for(int x=xIndex1-1;x>=0;x--){
				   Color c=Color.white;
				   if(getChess(x,yIndex1,c)!=null){
					   continueCount2++;
				   }else
				   { if(continueCount2>=2&&getChess(x,yIndex1,Color.black)==null&&getChess(x+3,yIndex1,Color.black)==null){
				    	Point ch1=new Point(x,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;
				    }
			    	  break;} 
			   }
		   //横向向东寻找
		    for(int x=xIndex1+1;x<=COLS;x++){
			      Color c=Color.white;
			      if(getChess(x,yIndex1,c)!=null){
				     continueCount2++;
			      }else
			      { if(continueCount2>=2&&getChess(x,yIndex1,Color.black)==null&&getChess(x-3,yIndex1,Color.black)==null){
				    	Point ch1=new Point(x,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;
				    }
			    	  break;} 
		    }
		   
			   continueCount2=1;
		    
		    //继续另一种搜索纵向
		    //向上搜索
		    for(int y=yIndex1-1;y>=0;y--){
		 	   Color c=Color.white;
		 	   if(getChess(xIndex1,y,c)!=null){
		 		   continueCount2++;
		 	   }else
		 	  { if(continueCount2>=2&&getChess(xIndex1,y,Color.black)==null&&getChess(xIndex1,y+3,Color.black)==null){
			    	Point ch1=new Point(xIndex1,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    //纵向向下寻找
		    for(int y=yIndex1+1;y<=ROWS;y++){
		 	   Color c=Color.white;
		 	   if(getChess(xIndex1,y,c)!=null)
		 	       continueCount2++;
		        else
		        { if(continueCount2>=2&&getChess(xIndex1,y,Color.black)==null&&getChess(xIndex1,y-3,Color.black)==null){
			    	Point ch1=new Point(xIndex1,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    
		    }
		   
		 	   continueCount2=1;
		    
		    
		    //继续另一种情况的搜索：斜向
		    //东北寻找
		    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
		 	   Color c=Color.white;
		 	   if(getChess(x,y,c)!=null){
		 		   continueCount2++;
		 	   }
		 	   else  { if(continueCount2>=2&&getChess(x,y,Color.black)==null&&getChess(x-3,y+3,Color.black)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    //西南寻找
		    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
		 	   Color c=Color.white;
		 	   if(getChess(x,y,c)!=null){
		 		   continueCount2++;
		 	   }
		 	   else  { if(continueCount2>=2&&getChess(x,y,Color.black)==null&&getChess(x+3,y-3,Color.black)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    continueCount2=1;
		    
		    
		    //继续另一种情况的搜索：斜向
		    //西北寻找
		    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
		 	   Color c=Color.white;
		 	   if(getChess(x,y,c)!=null)
		 		   continueCount2++;
		 	   else { if(continueCount2>=2&&getChess(x,y,Color.black)==null&&getChess(x+3,y+3,Color.black)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    //东南寻找
		    for(int x=xIndex1+1,y=yIndex1+1;x<=COLS&&y<=ROWS;x++,y++){
		 	   Color c=Color.white;
		 	   if(getChess(x,y,c)!=null)
		 		   continueCount2++;
		 	   else  { if(continueCount2>=2&&getChess(x,y,Color.black)==null&&getChess(x-3,y-3,Color.black)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    continueCount2=1;}}	    
		    return false;
		  }
	   
       private boolean gotwo(){
			   for(int j=0;j<chessCount;j++)
			   {
			if(chessList[j].getColor().equals(Color.white))	   
			   {int continueCount2=1;//连续棋子的个数
			   int xIndex1,yIndex1;
			   xIndex1=chessList[j].getX();
			   yIndex1=chessList[j].getY();
			   //横向向西寻找
			   for(int x=xIndex1-1;x>=0;x--){
				   Color c=Color.white;
				   if(getChess(x,yIndex1,c)!=null){
					   continueCount2++;
				   }else
				   { if(continueCount2>=1&&getChess(x,yIndex1,Color.black)==null&&getChess(x+2,yIndex1,Color.black)==null){
				    	Point ch1=new Point(x,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;
				    }
			    	  break;} 
			   }
		   //横向向东寻找
		    for(int x=xIndex1+1;x<=COLS;x++){
			      Color c=Color.white;
			      if(getChess(x,yIndex1,c)!=null){
				     continueCount2++;
			      }else
			      { if(continueCount2>=1&&getChess(x,yIndex1,Color.black)==null&&getChess(x-2,yIndex1,Color.black)==null){
				    	Point ch1=new Point(x,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;
				    }
			    	  break;} 
		    }
		   
			   continueCount2=1;
		    
		    //继续另一种搜索纵向
		    //向上搜索
		    for(int y=yIndex1-1;y>=0;y--){
		 	   Color c=Color.white;
		 	   if(getChess(xIndex1,y,c)!=null){
		 		   continueCount2++;
		 	   }else
		 	  { if(continueCount2>=1&&getChess(xIndex1,y,Color.black)==null&&getChess(xIndex1,y+2,Color.black)==null){
			    	Point ch1=new Point(xIndex1,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    //纵向向下寻找
		    for(int y=yIndex1+1;y<=ROWS;y++){
		 	   Color c=Color.white;
		 	   if(getChess(xIndex1,y,c)!=null)
		 	       continueCount2++;
		        else
		        { if(continueCount2>=1&&getChess(xIndex1,y,Color.black)==null&&getChess(xIndex1,y-2,Color.black)==null){
			    	Point ch1=new Point(xIndex1,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    
		    }
		   
		 	   continueCount2=1;
		    
		    
		    //继续另一种情况的搜索：斜向
		    //东北寻找
		    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
		 	   Color c=Color.white;
		 	   if(getChess(x,y,c)!=null){
		 		   continueCount2++;
		 	   }
		 	   else  { if(continueCount2>=1&&getChess(x,y,Color.black)==null&&getChess(x-2,y+2,Color.black)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    //西南寻找
		    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
		 	   Color c=Color.white;
		 	   if(getChess(x,y,c)!=null){
		 		   continueCount2++;
		 	   }
		 	   else  { if(continueCount2>=1&&getChess(x,y,Color.black)==null&&getChess(x+2,y-2,Color.black)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    continueCount2=1;
		    
		    
		    //继续另一种情况的搜索：斜向
		    //西北寻找
		    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
		 	   Color c=Color.white;
		 	   if(getChess(x,y,c)!=null)
		 		   continueCount2++;
		 	   else { if(continueCount2>=1&&getChess(x,y,Color.black)==null&&getChess(x+2,y+2,Color.black)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    //东南寻找
		    for(int x=xIndex1+1,y=yIndex1+1;x<=COLS&&y<=ROWS;x++,y++){
		 	   Color c=Color.white;
		 	   if(getChess(x,y,c)!=null)
		 		   continueCount2++;
		 	   else  { if(continueCount2>=1&&getChess(x,y,Color.black)==null&&getChess(x-2,y-2,Color.black)==null){
			    	Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;
			    }
		    	  break;} 
		    }
		    continueCount2=1;}}	    
		    return false;
		  }  
       
       private boolean isgewu(){
    	   for(int u=0;u<chessCount;u++)
		   {
    		   if(chessList[u].getColor().equals(Color.black))	 
    		   {int continueCount3=1;//连续棋子的个数		  
		   //横向向西寻找
    		   int xIndex1,yIndex1;
			   xIndex1=chessList[u].getX();
			   yIndex1=chessList[u].getY();
		   for(int x=xIndex1-1;x>=0;x--){
			   Color c=Color.black;
			   if(getChess(x,yIndex1,c)!=null){
				   continueCount3++;
			   }else
			   { if(continueCount3>=3&&getChess(x,yIndex1,Color.white)==null&&getChess(x+4,yIndex1,Color.white)==null)
			   {   if(getChess(x+5,yIndex1,Color.black)!=null)
			    	{Point ch1=new Point(x+4,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
			   else
			   {if(getChess(x-1,yIndex1,Color.black)!=null)
		    	{Point ch1=new Point(x,yIndex1,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}}
			    }
			   else
			   {
				   if(continueCount3>=3&&(getChess(x,yIndex1,Color.white)==null||getChess(x+4,yIndex1,Color.white)==null))
				   {
					   if(getChess(x,yIndex1,Color.white)!=null)
					   {if(getChess(x+5,yIndex1,Color.black)!=null)
				    	{Point ch1=new Point(x+4,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}}
					   else
					   {
						   if(getChess(x+4,yIndex1,Color.white)!=null)
						   {if(getChess(x-1,yIndex1,Color.black)!=null)
					    	{Point ch1=new Point(x,yIndex1,Color.white);
							   chessList[chessCount++]=ch1;
							    repaint();
							    return true;}}
					   }
				   }
			   }
		    	  break;} 
		   }
	   //横向向东寻找
	    for(int x=xIndex1+1;x<=COLS;x++){
		      Color c=Color.black;
		      if(getChess(x,yIndex1,c)!=null){
			     continueCount3++;
		      }else
		      { if(continueCount3>=3&&getChess(x,yIndex1,Color.white)==null&&getChess(x-4,yIndex1,Color.white)==null)
		      {   
		    	  if(getChess(x-5,yIndex1,Color.black)!=null)
		    	  {Point ch1=new Point(x-4,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
		    	  else
		    	  {if(getChess(x+1,yIndex1,Color.black)!=null)
		    	  {Point ch1=new Point(x,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}}
		     
			    }
		      else
			   {
				   if(continueCount3>=3&&(getChess(x,yIndex1,Color.white)==null||getChess(x-4,yIndex1,Color.white)==null))
				   {
					   if(getChess(x,yIndex1,Color.white)!=null)
					   {if(getChess(x-5,yIndex1,Color.black)!=null)
				    	{Point ch1=new Point(x-4,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}}
					   else
					   {
						   if(getChess(x-4,yIndex1,Color.white)!=null)
						   {if(getChess(x+1,yIndex1,Color.black)!=null)
					    	{Point ch1=new Point(x,yIndex1,Color.white);
							   chessList[chessCount++]=ch1;
							    repaint();
							    return true;}}
					   }
				   }
			   }
		    	  break;} 
	    }
	   
		   continueCount3=1;
	    
	    //继续另一种搜索纵向
	    //向上搜索
	    for(int y=yIndex1-1;y>=0;y--){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null){
	 		   continueCount3++;
	 	   }else
	 	  { if(continueCount3>=3&&getChess(xIndex1,y,Color.white)==null&&getChess(xIndex1,y+4,Color.white)==null)
	 	  {
		    	if(getChess(xIndex1,y-1,c)!=null)
	 		  {Point ch1=new Point(xIndex1,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	else
		    	{
		    		if(getChess(xIndex1,y+5,c)!=null)
		    		{Point ch1=new Point(xIndex1,y+4,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
		    	}
		    }
	 	  else
	 	  {
	 		 if(continueCount3>=3&&(getChess(xIndex1,y,Color.white)==null||getChess(xIndex1,y+4,Color.white)==null))
	 		 {
	 			 if(getChess(xIndex1,y,Color.white)!=null)
	 			 {
	 				 if(getChess(xIndex1,y+5,c)!=null)
	 				{Point ch1=new Point(xIndex1,y+4,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
	 			 }
	 			 else
	 			 {
	 				if(getChess(xIndex1,y+4,Color.white)!=null)
	 				{if(getChess(xIndex1,y-1,c)!=null)
	 		 		  {Point ch1=new Point(xIndex1,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}}
	 			 }
	 			 
	 		 }
	 	  }
	    	  break;} 
	    }
	    //纵向向下寻找
	    for(int y=yIndex1+1;y<=ROWS;y++){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null)
	 	       continueCount3++;
	        else
	        { if(continueCount3>=3&&getChess(xIndex1,y,Color.white)==null&&getChess(xIndex1,y-4,Color.white)==null)
	        {   if(getChess(xIndex1,y-5,c)!=null)
		    	{Point ch1=new Point(xIndex1,y-4,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;	}	
	        else
	        { if(getChess(xIndex1,y+1,c)!=null)
	    	{Point ch1=new Point(xIndex1,y,Color.white);
			   chessList[chessCount++]=ch1;
			    repaint();
			    return true;	}	}	
		    }
	        
	        else
		 	  {
		 		 if(continueCount3>=3&&(getChess(xIndex1,y,Color.white)==null||getChess(xIndex1,y-4,Color.white)==null))
		 		 {
		 			 if(getChess(xIndex1,y,Color.white)!=null)
		 			 {
		 				 if(getChess(xIndex1,y-5,c)!=null)
		 				{Point ch1=new Point(xIndex1,y-4,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}
		 			 }
		 			 else
		 			 {
		 				if(getChess(xIndex1,y-4,Color.white)!=null)
		 				{if(getChess(xIndex1,y+1,c)!=null)
		 		 		  {Point ch1=new Point(xIndex1,y,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}}
		 			 }
		 			 
		 		 }
		 	  }
	    	  break;} 
	    
	    }
	   
	 	   continueCount3=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //东北寻找
	    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount3++;
	 	   }
	 	   else  { if(continueCount3>=3&&getChess(x,y,Color.white)==null&&getChess(x-4,y+4,Color.white)==null)
	 	   {
		    	if(getChess(x+1,y-1,c)!=null)
		    		{Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	else
		    	{
		    		if(getChess(x-5,y+5,c)!=null)
		    		{Point ch1=new Point(x-4,y+4,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	}
		    }
	 	   else
	 	   {
	 		  if(continueCount3>=3&&(getChess(x,y,Color.white)==null||getChess(x-4,y+4,Color.white)==null))
	 		  {
	 			  if(getChess(x,y,Color.white)!=null)
	 			  {
	 				 if(getChess(x-5,y+5,c)!=null)
			    		{Point ch1=new Point(x-4,y+4,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
	 			  }
	 			  else
	 			  {
	 				  if(getChess(x-4,y+4,Color.white)!=null)
                        {if(getChess(x+1,y-1,c)!=null)
    		    		{Point ch1=new Point(x,y,Color.white);
    					   chessList[chessCount++]=ch1;
    					    repaint();
    					    return true;}}
	 			  }
	 		  }
	 		   }
	    	  break;} 
	    }
	    //西南寻找
	    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount3++;
	 	   }
	 	   else  { if(continueCount3>=3&&getChess(x,y,Color.white)==null&&getChess(x+4,y-4,Color.white)==null)
	 	   {   if(getChess(x-1,y+1,c)!=null)
	 	     { Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
	 	   else
	 	   {if(getChess(x+5,y-5,c)!=null)
	 	     { Point ch1=new Point(x+4,y-4,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}}
	 	   
		    }
	 	   else
	 	   {
	 		   if(continueCount3>=3&&(getChess(x,y,Color.white)==null||getChess(x+4,y-4,Color.white)==null))
	 		   {
	 			   if(getChess(x,y,Color.white)!=null)
	 			   {
	 				  if(getChess(x+5,y-5,c)!=null)
	 		 	     { Point ch1=new Point(x+4,y-4,Color.white);
	 					   chessList[chessCount++]=ch1;
	 					    repaint();
	 					    return true;}
	 			   }
	 			   else
	 			   {
	 				   if(getChess(x+4,y-4,Color.white)!=null)
	 				   { if(getChess(x-1,y+1,c)!=null)
	 			 	     { Point ch1=new Point(x,y,Color.white);
	 					   chessList[chessCount++]=ch1;
	 					    repaint();
	 					    return true;}}
	 			   }
	 		   }
	 	   
	 	   
	 	   }
	    	  break;} 
	    }
	    continueCount3=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //西北寻找
	    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount3++;
	 	   else { if(continueCount3>=3&&getChess(x,y,Color.white)==null&&getChess(x+4,y+4,Color.white)==null){
		    	if(getChess(x-1,y-1,c)!=null)
		    		{Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	else
		    	{
		    		if(getChess(x+5,y+5,c)!=null)
		    		{Point ch1=new Point(x+4,y+4,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	}
		    }
	 	   else
	 	   {
	 		  if(continueCount3>=3&&(getChess(x,y,Color.white)==null||getChess(x+4,y+4,Color.white)==null))
	 		  {
	 			  if(getChess(x,y,Color.white)!=null)
	 			  {
	 				 if(getChess(x+5,y+5,c)!=null)
			    		{Point ch1=new Point(x+4,y+4,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
	 			  }
	 			  else
	 			  {
	 				  if(getChess(x+4,y+4,Color.white)!=null)
	 				  {
	 					 if(getChess(x-1,y-1,c)!=null)
	 		    		{Point ch1=new Point(x,y,Color.white);
	 				   chessList[chessCount++]=ch1;
	 				    repaint();
	 				    return true;}
	 				  }
	 			  }
	 		  }
	 	   }
	    	  break;} 
	    }
	    //东南寻找
	    for(int x=xIndex1+1,y=yIndex1+1;x<=COLS&&y<=ROWS;x++,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount3++;
	 	   else  { if(continueCount3>=3&&getChess(x,y,Color.white)==null&&getChess(x-4,y-4,Color.white)==null)
	 	   {    if(getChess(x+1,y+1,c)!=null)
		    	{Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
	 	   else
	 	   {
	 		  if(getChess(x-5,y-5,c)!=null)
		    	{Point ch1=new Point(x-4,y-4,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
	 	   }
		    	
		    }
	 	   else
	 	   {
	 		  if(continueCount3>=3&&(getChess(x,y,Color.white)==null||getChess(x-4,y-4,Color.white)==null))
	 		  {
	 			  if(getChess(x,y,Color.white)!=null)
	 			  {
	 				 if(getChess(x-5,y-5,c)!=null)
	 		    	{Point ch1=new Point(x-4,y-4,Color.white);
	 				   chessList[chessCount++]=ch1;
	 				    repaint();
	 				    return true;}
	 			  }
	 			  else
	 			  {
	 				  if(getChess(x-4,y-4,Color.white)!=null)
	 				  {
	 					 if(getChess(x+1,y+1,c)!=null)
	 			    	{Point ch1=new Point(x,y,Color.white);
	 					   chessList[chessCount++]=ch1;
	 					    repaint();
	 					    return true;}
	 				  }
	 			  }
	 		  }
	 	   }
	    	  break;} 
	    }
	    continueCount3=1;}	}    
	    return false;
	  }

       private boolean isgetwotwo(){
    	   for(int j=0;j<chessCount;j++)
		   {   
    		   if(chessList[j].getColor().equals(Color.black))
    		   { int continueCount1=1;//连续棋子的个数
    		   int xIndex1,yIndex1;
			   xIndex1=chessList[j].getX();
			   yIndex1=chessList[j].getY();
		   //横向向西寻找
		   for(int x=xIndex1-1;x>=0;x--){
			   Color c=Color.black;
			   if(getChess(x,yIndex1,c)!=null){
				   continueCount1++;
			   }else
			   { if(continueCount1>=2&&getChess(x,yIndex1,Color.white)==null&&getChess(x+3,yIndex1,Color.white)==null){
if((getChess(x+1,yIndex1-1,c)!=null&&getChess(x+2,yIndex1-2,c)!=null&&getChess(x+3,yIndex1-3,Color.white)==null)||(getChess(x,yIndex1-1,c)!=null&&getChess(x,yIndex1-2,c)!=null&&getChess(x,yIndex1-3,Color.white)==null)||(getChess(x-1,yIndex1-1,c)!=null&&getChess(x-2,yIndex1-2,c)!=null&&getChess(x-3,yIndex1-3,Color.white)==null)||(getChess(x-1,yIndex1,c)!=null&&getChess(x-2,yIndex1,c)!=null&&getChess(x-3,yIndex1,Color.white)==null)||(getChess(x-1,yIndex1+1,c)!=null&&getChess(x-2,yIndex1+2,c)!=null&&getChess(x-3,yIndex1+3,Color.white)==null)||(getChess(x,yIndex1+1,c)!=null&&getChess(x,yIndex1+2,c)!=null&&getChess(x,yIndex1+3,Color.white)==null)||(getChess(x+1,yIndex1+1,c)!=null&&getChess(x+2,yIndex1+2,c)!=null&&getChess(x+3,yIndex1+3,Color.white)==null))
				   {Point ch1=new Point(x,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
			    }
		    	  break;} 
		   }
	   //横向向东寻找
	    for(int x=xIndex1+1;x<=COLS;x++){
		      Color c=Color.black;
		      if(getChess(x,yIndex1,c)!=null){
			     continueCount1++;
		      }else
		      { if(continueCount1>=2&&getChess(x,yIndex1,Color.white)==null&&getChess(x-3,yIndex1,Color.white)==null){
if((getChess(x+1,yIndex1-1,c)!=null&&getChess(x+2,yIndex1-2,c)!=null&&getChess(x+3,yIndex1-3,Color.white)==null)||(getChess(x,yIndex1-1,c)!=null&&getChess(x,yIndex1-2,c)!=null&&getChess(x,yIndex1-3,Color.white)==null)||(getChess(x-1,yIndex1-1,c)!=null&&getChess(x-2,yIndex1-2,c)!=null&&getChess(x-3,yIndex1-3,Color.white)==null)||(getChess(x+1,yIndex1,c)!=null&&getChess(x+2,yIndex1,c)!=null&&getChess(x+3,yIndex1,Color.white)==null)||(getChess(x-1,yIndex1+1,c)!=null&&getChess(x-2,yIndex1+2,c)!=null&&getChess(x-3,yIndex1+3,Color.white)==null)||(getChess(x,yIndex1+1,c)!=null&&getChess(x,yIndex1+2,c)!=null&&getChess(x,yIndex1+3,Color.white)==null)||(getChess(x+1,yIndex1+1,c)!=null&&getChess(x+2,yIndex1+2,c)!=null&&getChess(x+3,yIndex1+3,Color.white)==null))			    	
		    	  {Point ch1=new Point(x,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
			    }
		    	  break;} 
	    }
	   
		   continueCount1=1;
	    
	    //继续另一种搜索纵向
	    //向上搜索
	    for(int y=yIndex1-1;y>=0;y--){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null){
	 		   continueCount1++;
	 	   }else
	 	  { if(continueCount1>=2&&getChess(xIndex1,y+3,Color.white)==null&&getChess(xIndex1,y,Color.white)==null){
if((getChess(xIndex1+1,y-1,c)!=null&&getChess(xIndex1+2,y-2,c)!=null&&getChess(xIndex1+3,y-3,Color.white)==null)||(getChess(xIndex1,y-1,c)!=null&&getChess(xIndex1,y-2,c)!=null&&getChess(xIndex1,y-3,Color.white)==null)||(getChess(xIndex1-1,y-1,c)!=null&&getChess(xIndex1-2,y-2,c)!=null&&getChess(xIndex1-3,y-3,Color.white)==null)||(getChess(xIndex1+1,y,c)!=null&&getChess(xIndex1+2,y,c)!=null&&getChess(xIndex1+3,y,Color.white)==null)||(getChess(xIndex1-1,y+1,c)!=null&&getChess(xIndex1-2,y+2,c)!=null&&getChess(xIndex1-3,y+3,Color.white)==null)||(getChess(xIndex1-1,y,c)!=null&&getChess(xIndex1-2,y,c)!=null&&getChess(xIndex1-3,y,Color.white)==null)||(getChess(xIndex1+1,y+1,c)!=null&&getChess(xIndex1+2,y+2,c)!=null&&getChess(xIndex1+3,y+3,Color.white)==null))			    			    	
	 		  {Point ch1=new Point(xIndex1,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    }
	    	  break;} 
	    }
	    //纵向向下寻找
	    for(int y=yIndex1+1;y<=ROWS;y++){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null)
	 	       continueCount1++;
	        else
	        { if(continueCount1>=2&&getChess(xIndex1,y-3,Color.white)==null&&getChess(xIndex1,y,Color.white)==null){
if((getChess(xIndex1+1,y-1,c)!=null&&getChess(xIndex1+2,y-2,c)!=null&&getChess(xIndex1+3,y-3,Color.white)!=null)||(getChess(xIndex1,y+1,c)!=null&&getChess(xIndex1,y+2,c)!=null&&getChess(xIndex1,y+3,Color.white)!=null)||(getChess(xIndex1-1,y-1,c)!=null&&getChess(xIndex1-2,y-2,c)!=null&&getChess(xIndex1-3,y-3,Color.white)!=null)||(getChess(xIndex1+1,y,c)!=null&&getChess(xIndex1+2,y,c)!=null&&getChess(xIndex1+3,y,Color.white)!=null)||(getChess(xIndex1-1,y+1,c)!=null&&getChess(xIndex1-2,y+2,c)!=null&&getChess(xIndex1-3,y+3,Color.white)!=null)||(getChess(xIndex1-1,y,c)!=null&&getChess(xIndex1-2,y,c)!=null&&getChess(xIndex1-3,y,Color.white)!=null)||(getChess(xIndex1+1,y+1,c)!=null&&getChess(xIndex1+2,y+2,c)!=null&&getChess(xIndex1+3,y+3,Color.white)!=null))		    	
	        	{Point ch1=new Point(xIndex1,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    }
	    	  break;} 
	    
	    }
	   
	 	   continueCount1=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //东北寻找
	    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount1++;
	 	   }
	 	   else  { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x-3,y+3,Color.white)==null){
if((getChess(x+1,y-1,c)!=null&&getChess(x+2,y-2,c)!=null&&getChess(x+3,y-3,Color.white)==null)||(getChess(x,y+1,c)!=null&&getChess(x,y+2,c)==null&&getChess(x,y+3,Color.white)==null)||(getChess(x-1,y-1,c)!=null&&getChess(x-2,y-2,c)!=null&&getChess(x-3,y-3,Color.white)==null)||(getChess(x+1,y,c)!=null&&getChess(x+2,y,c)!=null&&getChess(x+3,y,Color.white)==null)||(getChess(x,y-1,c)!=null&&getChess(x,y-2,c)!=null&&getChess(x,y-3,Color.white)==null)||(getChess(x-1,y,c)!=null&&getChess(x-2,y,c)!=null&&getChess(x-3,y,Color.white)==null)||(getChess(x+1,y+1,c)!=null&&getChess(x+2,y+2,c)!=null&&getChess(x+3,y+3,Color.white)==null))		    	
	 		   {Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    }
	    	  break;} 
	    }
	    //西南寻找
	    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount1++;
	 	   }
	 	   else  { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x+3,y-3,Color.white)==null){
if((getChess(x-1,y+1,c)!=null&&getChess(x-2,y+2,c)!=null&&getChess(x-3,y+3,Color.white)==null)||(getChess(x,y+1,c)!=null&&getChess(x,y+2,c)!=null&&getChess(x,y+3,Color.white)==null)||(getChess(x-1,y-1,c)!=null&&getChess(x-2,y-2,c)!=null&&getChess(x-3,y-3,Color.white)==null)||(getChess(x+1,y,c)!=null&&getChess(x+2,y,c)!=null&&getChess(x+3,y,Color.white)==null)||(getChess(x,y-1,c)!=null&&getChess(x,y-2,c)!=null&&getChess(x,y-3,Color.white)==null)||(getChess(x-1,y,c)!=null&&getChess(x-2,y,c)!=null&&getChess(x-3,y,Color.white)==null)||(getChess(x+1,y+1,c)!=null&&getChess(x+2,y+2,c)!=null&&getChess(x+3,y+3,Color.white)==null))		    			    	
	 		   {Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    }
	    	  break;} 
	    }
	    continueCount1=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //西北寻找
	    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount1++;
	 	   else { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x+3,y+3,Color.white)==null){
if((getChess(x-1,y+1,c)!=null&&getChess(x-2,y+2,c)!=null&&getChess(x-3,y+3,Color.white)==null)||(getChess(x,y+1,c)!=null&&getChess(x,y+2,c)!=null&&getChess(x,y+3,Color.white)==null)||(getChess(x-1,y-1,c)!=null&&getChess(x-2,y-2,c)!=null&&getChess(x-3,y-3,Color.white)==null)||(getChess(x+1,y,c)!=null&&getChess(x+2,y,c)!=null&&getChess(x+3,y,Color.white)==null)||(getChess(x,y-1,c)!=null&&getChess(x,y-2,c)!=null&&getChess(x,y-3,Color.white)==null)||(getChess(x-1,y,c)!=null&&getChess(x-2,y,c)!=null&&getChess(x-3,y,Color.white)==null)||(getChess(x+1,y-1,c)!=null&&getChess(x+2,y-2,c)!=null&&getChess(x+3,y-3,Color.white)==null))		    			    		
	 		   {Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    }
	    	  break;} 
	    }
	    //东南寻找
	    for(int x=xIndex1+1,y=yIndex1+1;x<=COLS&&y<=ROWS;x++,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount1++;
	 	   else  { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x-3,y-3,Color.white)==null){
	 		  if((getChess(x-1,y+1,c)!=null&&getChess(x-2,y+2,c)!=null&&getChess(x-3,y+3,Color.white)==null)||(getChess(x,y+1,c)!=null&&getChess(x,y+2,c)!=null&&getChess(x,y+3,Color.white)==null)||(getChess(x+1,y-1,c)!=null&&getChess(x+2,y-2,c)!=null&&getChess(x+3,y-3,Color.white)==null)||(getChess(x+1,y,c)!=null&&getChess(x+2,y,c)!=null&&getChess(x+3,y,Color.white)==null)||(getChess(x,y-1,c)!=null&&getChess(x,y-2,c)!=null&&getChess(x,y-3,Color.white)==null)||(getChess(x-1,y,c)!=null&&getChess(x-2,y,c)!=null&&getChess(x-3,y,Color.white)==null)||(getChess(x+1,y-1,c)!=null&&getChess(x+2,y-2,c)!=null&&getChess(x+3,y-3,Color.white)==null))		    			    				    	
	 		   {Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    }
	    	  break;} 
	    }
	    continueCount1=1;}	}    
	    return false;
	  }
  
       private boolean isgethree(){
    	   for(int j=0;j<chessCount;j++)
		   {   
    		   if(chessList[j].getColor().equals(Color.black))
    		   { int continueCount1=1;//连续棋子的个数
    		   int xIndex1,yIndex1;
			   xIndex1=chessList[j].getX();
			   yIndex1=chessList[j].getY();
		   
		  
		   //横向向西寻找
		   for(int x=xIndex1-1;x>=0;x--){
			   Color c=Color.black;
			   if(getChess(x,yIndex1,c)!=null){
				   continueCount1++;
			   }else
			   { if(continueCount1>=2&&getChess(x,yIndex1,Color.white)==null&&getChess(x+3,yIndex1,Color.white)==null){
			    	if(getChess(x-1,yIndex1,Color.black)!=null)
				   {Point ch1=new Point(x,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
			    	else
			    	{if(getChess(x+4,yIndex1,Color.black)!=null)
					   {Point ch1=new Point(x+3,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}}
			    }
		    	  break;} 
		   }
	   //横向向东寻找
	    for(int x=xIndex1+1;x<=COLS;x++){
		      Color c=Color.black;
		      if(getChess(x,yIndex1,c)!=null){
			     continueCount1++;
		      }else
		      { if(continueCount1>=2&&getChess(x,yIndex1,Color.white)==null&&getChess(x-3,yIndex1,Color.white)==null){
		    	  if(getChess(x+1,yIndex1,Color.black)!=null)
		    	  {Point ch1=new Point(x,yIndex1,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
		    	  else
			    	{if(getChess(x-4,yIndex1,Color.black)!=null)
					   {Point ch1=new Point(x-3,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}}
			    }
		    	  break;} 
	    }
	   
		   continueCount1=1;
	    
	    //继续另一种搜索纵向
	    //向上搜索
	    for(int y=yIndex1-1;y>=0;y--){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null){
	 		   continueCount1++;
	 	   }else
	 	  { if(continueCount1>=2&&getChess(xIndex1,y+3,Color.white)==null&&getChess(xIndex1,y,Color.white)==null){
		    	if(getChess(xIndex1,y-1,Color.black)!=null)
	 		  {Point ch1=new Point(xIndex1,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	else
		    	{if(getChess(xIndex1,y+4,Color.black)!=null)
		 		  {Point ch1=new Point(xIndex1,y+3,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}}
		    }
	    	  break;} 
	    }
	    //纵向向下寻找
	    for(int y=yIndex1+1;y<=ROWS;y++){
	 	   Color c=Color.black;
	 	   if(getChess(xIndex1,y,c)!=null)
	 	       continueCount1++;
	        else
	        { if(continueCount1>=2&&getChess(xIndex1,y-3,Color.white)==null&&getChess(xIndex1,y,Color.white)==null){
		    	if(getChess(xIndex1,y+1,Color.black)!=null)
	        	{Point ch1=new Point(xIndex1,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	else
		    	{
		    		if(getChess(xIndex1,y-4,Color.black)!=null)
			 		  {Point ch1=new Point(xIndex1,y-3,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
		    	}
		    }
	    	  break;} 
	    
	    }
	   
	 	   continueCount1=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //东北寻找
	    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount1++;
	 	   }
	 	   else  { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x-3,y+3,Color.white)==null){
		    	if(getChess(x+1,y-1,Color.black)!=null)
	 		   {Point ch1=new Point(x,y,Color.white);
				   chessList[chessCount++]=ch1;
				    repaint();
				    return true;}
		    	else
		    	{if(getChess(x-4,y+4,Color.black)!=null)
		 		   {Point ch1=new Point(x-3,y+3,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}}
		    }
	    	  break;} 
	    }
	    //西南寻找
	    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null){
	 		   continueCount1++;
	 	   }
	 	   else  { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x+3,y-3,Color.white)==null){
	 			if(getChess(x-1,y+1,Color.black)!=null)
		 		   {Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
			    	else
			    	{if(getChess(x+4,y-4,Color.black)!=null)
			 		   {Point ch1=new Point(x+3,y-3,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}}
			    }
	    	  break;} 
	    }
	    continueCount1=1;
	    
	    
	    //继续另一种情况的搜索：斜向
	    //西北寻找
	    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount1++;
	 	   else { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x+3,y+3,Color.white)==null){
	 			if(getChess(x-1,y-1,Color.black)!=null)
		 		   {Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
			    	else
			    	{if(getChess(x-4,y-4,Color.black)!=null)
			 		   {Point ch1=new Point(x-3,y-3,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}}
			    }
	    	  break;} 
	    }
	    //东南寻找
	    for(int x=xIndex1+1,y=yIndex1+1;x<=COLS&&y<=ROWS;x++,y++){
	 	   Color c=Color.black;
	 	   if(getChess(x,y,c)!=null)
	 		   continueCount1++;
	 	   else  { if(continueCount1>=2&&getChess(x,y,Color.white)==null&&getChess(x-3,y-3,Color.white)==null){
	 			if(getChess(x+1,y+1,Color.black)!=null)
		 		   {Point ch1=new Point(x,y,Color.white);
					   chessList[chessCount++]=ch1;
					    repaint();
					    return true;}
			    	else
			    	{if(getChess(x-4,y+4,Color.black)!=null)
			 		   {Point ch1=new Point(x+3,y+3,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}}
			    }
	    	  break;} 
	    }
	    continueCount1=1;	}}    
	    return false;
	  }     

       private boolean iszhengfangxing()
       {
    	   for(int j=0;j<chessCount;j++)
		   {
		if(chessList[j].getColor().equals(Color.black))	   
		   {
		   int xIndex1,yIndex1;
		   xIndex1=chessList[j].getX();
		   yIndex1=chessList[j].getY();
		   if(getChess(xIndex1+1,yIndex1-1,Color.black)!=null&&getChess(xIndex1+2,yIndex1,Color.black)!=null&&getChess(xIndex1+1,yIndex1+1,Color.black)!=null&&(!findChess(xIndex1-1,yIndex1))&&(!findChess(xIndex1+1,yIndex1+2))&&(!findChess(xIndex1+3,yIndex1))&&(!findChess(xIndex1+1,yIndex1-2)))
		   {  if(!findChess(xIndex1+1,yIndex1))
			   {Point ch1=new Point(xIndex1+1,yIndex1,Color.white);
			   chessList[chessCount++]=ch1;
			    repaint();
			    return true;}
		   }
		   
		   }
		}
    	   return false;
       }
       
       private boolean gotruefive(){
		   
    	   for(int xIndex1=0;xIndex1<=15;xIndex1++)
    	   {
    		   for(int yIndex1=0;yIndex1<=15;yIndex1++)
    		   {
    			   if(!findChess(xIndex1,yIndex1))
    			   {
    				   int continueCount=0;//连续棋子的个数
    					  //东西寻找
    				   for(int x1=xIndex1-1;x1>=0;x1--)
    				   {
    					   Color c=Color.white;
    				 	   if(getChess(x1,yIndex1,c)!=null)
    				 		   continueCount++;
    				 	   else
    				 		   break;
    				   }
    				   for(int x1=xIndex1+1;x1<=15;x1--)
    				   {
    					   Color c=Color.white;
    				 	   if(getChess(x1,yIndex1,c)!=null)
    				 		   continueCount++;
    				 	  else
   				 		   break;
    				   }
    				   if(continueCount>=4)
    				   {
    					   Point ch1=new Point(xIndex1,yIndex1,Color.white);
    					   chessList[chessCount++]=ch1;
    					    repaint();
    					    return true;
    					   }
    				   //南北寻找
    				   continueCount=0;  
    				   for(int y1=yIndex1-1;y1>=0;y1--)
    				   {
    					   Color c=Color.white;
    				 	   if(getChess(xIndex1,y1,c)!=null)
    				 		   continueCount++;
    				 	   else
    				 		   break;
    				   }
    				   for(int y1=yIndex1+1;y1<=ROWS;y1++)
    				   {
    					   Color c=Color.white;
    				 	   if(getChess(xIndex1,y1,c)!=null)
    				 		   continueCount++;
    				 	  else
   				 		   break;
    				   }
    				   if(continueCount>=4)
    				   {
    					   Point ch1=new Point(xIndex1,yIndex1,Color.white);
    					   chessList[chessCount++]=ch1;
    					    repaint();
    					    return true;
    					   }
    				   //西北东南寻找
    				   continueCount=0;  
    				   for(int x1=xIndex1-1,y1=yIndex1-1;x1>=0&&y1>=0;x1--,y1--)
    				   {
    					   Color c=Color.white;
    				 	   if(getChess(x1,y1,c)!=null)
    				 		   continueCount++;
    				 	   else
    				 		   break;
    				   }
    				   for(int x1=xIndex1+1,y1=yIndex1+1;x1<=ROWS&&y1<=COLS;x1++,y1++)
    				   {
    					   Color c=Color.white;
    				 	   if(getChess(x1,y1,c)!=null)
    				 		   continueCount++;
    				 	  else
   				 		   break;
    				   }
    				   if(continueCount>=4)
    				   {
    					   Point ch1=new Point(xIndex1,yIndex1,Color.white);
    					   chessList[chessCount++]=ch1;
    					    repaint();
    					    return true;
    					   }
    				   //西南东北寻找
    				   continueCount=0;  
    				   for(int x1=xIndex1-1,y1=yIndex1+1;x1>=0&&y1<=COLS;x1--,y1++)
    				   {
    					   Color c=Color.white;
    				 	   if(getChess(x1,y1,c)!=null)
    				 		   continueCount++;
    				 	   else
    				 		   break;
    				   }
    				   for(int x1=xIndex1+1,y1=yIndex1-1;x1<=ROWS&&y1>=0;x1++,y1--)
    				   {
    					   Color c=Color.white;
    				 	   if(getChess(x1,y1,c)!=null)
    				 		   continueCount++;
    				 	  else
   				 		   break;
    				   }
    				   if(continueCount>=4)
    				   {
    					   Point ch1=new Point(xIndex1,yIndex1,Color.white);
    					   chessList[chessCount++]=ch1;
    					    repaint();
    					    return true;
    					   }
    			   }
    		   }
    		   
    	   }
    		   
    	   
	    return false;
	  }  
       
 private boolean gotruefour(){
		   
		   for(int xIndex1=0;xIndex1<=15;xIndex1++)
		   {
			   for(int yIndex1=0;yIndex1<=15;yIndex1++)
			   {
				   if(!findChess(xIndex1,yIndex1))
				   {   int tx1=0,tx2=0,ty1=0,ty2=0;
					   int continueCount=0;//连续棋子的个数
						  //东西寻找
					   for(int x1=xIndex1-1;x1>=0;x1--)
					   {
						   Color c=Color.white;
					 	   if(getChess(x1,yIndex1,c)!=null)
					 		   continueCount++;
					 	   else
					 	   { tx1=x1;
					 		   break;}
					 		  
					   }
					   for(int x2=xIndex1+1;x2<=15;x2--)
					   {
						   Color c=Color.white;
					 	   if(getChess(x2,yIndex1,c)!=null)
					 		   continueCount++;
					 	  else
					 	  {tx2=x2;
					 	  break;}
				 		   
					   }
					   if(continueCount>=3)
					   {if(getChess(tx1,yIndex1,Color.black)==null&&getChess(tx2,yIndex1,Color.black)==null)
						   {Point ch1=new Point(xIndex1,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}
						   }
					   //南北寻找
					   continueCount=0;  
					   for(int y1=yIndex1-1;y1>=0;y1--)
					   {
						   Color c=Color.white;
					 	   if(getChess(xIndex1,y1,c)!=null)
					 		   continueCount++;
					 	   else
					 		   {ty1=y1;
					 		   break;}
					   }
					   for(int y1=yIndex1+1;y1<=ROWS;y1++)
					   {
						   Color c=Color.white;
					 	   if(getChess(xIndex1,y1,c)!=null)
					 		   continueCount++;
					 	  else
					 	 {ty2=y1;
				 		   break;}
					   }
					   if(continueCount>=3)
					   {  if(getChess(xIndex1,ty1,Color.black)==null&&getChess(xIndex1,ty2,Color.black)==null)
						   {Point ch1=new Point(xIndex1,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}
						   }
					   //西北东南寻找
					   continueCount=0;  
					   for(int x1=xIndex1-1,y1=yIndex1-1;x1>=0&&y1>=0;x1--,y1--)
					   {
						   Color c=Color.white;
					 	   if(getChess(x1,y1,c)!=null)
					 		   continueCount++;
					 	   else
					 	   { tx1=x1;ty1=y1;
					 		   break;}
					   }
					   for(int x1=xIndex1+1,y1=yIndex1+1;x1<=ROWS&&y1<=COLS;x1++,y1++)
					   {
						   Color c=Color.white;
					 	   if(getChess(x1,y1,c)!=null)
					 		   continueCount++;
					 	  else
					 	 { tx2=x1;ty2=y1;
				 		   break;}
					   }
					   if(continueCount>=3)
					   { if(getChess(tx1,ty1,Color.black)==null&&getChess(tx2,ty2,Color.black)==null)
						   {Point ch1=new Point(xIndex1,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}
						   }
					   //西南东北寻找
					   continueCount=0;  
					   for(int x1=xIndex1-1,y1=yIndex1+1;x1>=0&&y1<=COLS;x1--,y1++)
					   {
						   Color c=Color.white;
					 	   if(getChess(x1,y1,c)!=null)
					 		   continueCount++;
					 	   else
					 		   {tx1=x1;ty1=y1;
					 		   break;}
					   }
					   for(int x1=xIndex1+1,y1=yIndex1-1;x1<=ROWS&&y1>=0;x1++,y1--)
					   {
						   Color c=Color.white;
					 	   if(getChess(x1,y1,c)!=null)
					 		   continueCount++;
					 	  else
					 	 {tx2=x1;ty2=y1;
				 		   break;}
					   }
					   if(continueCount>=3)
					   {
						   if(getChess(tx1,ty1,Color.black)==null&&getChess(tx2,ty2,Color.black)==null)
						   {Point ch1=new Point(xIndex1,yIndex1,Color.white);
						   chessList[chessCount++]=ch1;
						    repaint();
						    return true;}
						   }
				   }
			   }
			   
		   }
			   
		   
	    return false;
	  }



public void artInt2()
   {  int pingfen[][] = new int[16][16];int max;int maxx,maxy;
       
	   for(int i=0;i<=COLS;i++)
	   {
		   for(int j=0;j<=ROWS;j++)
		   {  
			  if(!findChess(i,j))
			  pingfen[i][j]=getpingfen(i,j);					
		   }		   
	   }
	   max=-999999999;
	   maxx=0;maxy=0;
	   for(int i=0;i<=COLS;i++)
	   {
		   for(int j=0;j<=ROWS;j++)
		   {  
			 if(pingfen[i][j]>max&&!findChess(i,j))		
			 { max=pingfen[i][j];
			  maxx=i;
			  maxy=j;
			 }
		   }		   
	   }
	   
	
	   System.out.println(max);
	   Point ch1=new Point(maxx,maxy,Color.white);
	   chessList[chessCount++]=ch1;
	    repaint();
   }
 
  public int getpingfen(int x,int y)
  {   int points=0;
	  Point ch1=new Point(x,y,Color.white);
	  chessList[chessCount++]=ch1;
	  points=points+yige(1); System.out.print(points+" ");
	  //points=points+huo2(1); System.out.print(points+" ");
	  //points=points+huosan(1);
	  //points=points+huosi(1);
	  //points=points+huo5(1);
	  //points=points+banhuoer(1);
	  //points=points+banhuosan(1);
	  //points=points+banhuosi(1);
	  //points=points+zhengfangxing(1);
	  //points=points+sibianxing(1);
	  //points=points+erjiaer(1);
	  //points=points+sanjiayi(1);
	  points=points+yige(0); System.out.print(points+" ");
	 // points=points+huo2(0); System.out.print(points+" ");
	  //points=points+huosan(0);
	  //points=points+huosi(0);
	  //points=points+huo5(0);
	  //points=points+banhuoer(0);
	  //points=points+banhuosan(0);
	  //points=points+banhuosi(0);
	  //points=points+zhengfangxing(0);
	  //points=points+sibianxing(0);
	  //points=points+erjiaer(0);
	  //points=points+sanjiayi(0);
	  chessList[chessCount-1]=null;
	   chessCount--;
	  System.out.println(points+" "+x+" "+y);
	  return points;
  }
  public int yige(int blackflag)
  {   int poi=0;
	  if(blackflag==1)  //扫黑的
	  {
		  for(int ll=0;ll<chessCount;ll++)
		  {   if(chessList[ll].getColor().equals(Color.black))
		  { int xIndex1,yIndex1;
			  xIndex1=chessList[ll].getX();
			  yIndex1=chessList[ll].getY();
			  if(xIndex1!=0&&xIndex1!=COLS&&yIndex1!=0&&yIndex1!=ROWS&&!findChess(xIndex1+1,yIndex1)&&(!findChess(xIndex1,yIndex1+1))&&(!findChess(xIndex1-1,yIndex1))&&(!findChess(xIndex1,yIndex1-1))&&(!findChess(xIndex1-1,yIndex1-1))&&(!findChess(xIndex1+1,yIndex1-1))&&(!findChess(xIndex1-1,yIndex1+1))&&(!findChess(xIndex1+1,yIndex1+1)))
			  {
				 poi=poi-1;
			  }}
		  }
	  }
	  
	  if(blackflag==0)  //扫白的
	  {
		  for(int ll=0;ll<chessCount;ll++)
		  {   if(chessList[ll].getColor().equals(Color.white))
		  { int xIndex1,yIndex1;
			  xIndex1=chessList[ll].getX();
			  yIndex1=chessList[ll].getY();
			  if(xIndex1!=0&&xIndex1!=COLS&&yIndex1!=0&&yIndex1!=ROWS&&!findChess(xIndex1+1,yIndex1)&&(!findChess(xIndex1,yIndex1+1))&&(!findChess(xIndex1-1,yIndex1))&&(!findChess(xIndex1,yIndex1-1))&&(!findChess(xIndex1-1,yIndex1-1))&&(!findChess(xIndex1+1,yIndex1-1))&&(!findChess(xIndex1-1,yIndex1+1))&&(!findChess(xIndex1+1,yIndex1+1)))
			  {
				 poi=poi+1;
			  }}
		  }
	  }
		  return poi;
  }
 
 public int yangben(int blackflag)
 {    int poi=0;Color c= new Color(0,0,0);int fenshu;
 if(blackflag==1)
 {c=Color.black;
   fenshu=-10;
 }
else
 {c=Color.white;
  fenshu=10;
 }
 {
	 int kucun[][]=new int[100][2];
     int geshu=0;
	 for(int ll=0;ll<chessCount;ll++)
	 {  
		 if(chessList[ll].getColor().equals(c))
		 {  
			 //neirong 
		 }
     }
	 }
	 return poi;
 }
  
 
 public int huo2(int blackflag)
 {   int poi=0;Color c= new Color(0,10,0);int fenshu;
	 if(blackflag==1)
		 {c=Color.black;
		   fenshu=-10;
		 }
	 else
		 {c=Color.white;
		  fenshu=10;
		 }
	 {   int kucun[][]=new int[100][2];
	     int geshu=-1;int cunzai = 0;
		 for(int ll=0;ll<chessCount;ll++)
		 {  
			 if(chessList[ll].getColor().equals(c))
			 {   
				 int continuecount=1;
				 int xIndex1,yIndex1;
				  xIndex1=chessList[ll].getX();
				  yIndex1=chessList[ll].getY();
				//横向向西寻找
				   for(int x=xIndex1-1;x>=0;x--){
					   
					   if(getChess(x,yIndex1,c)!=null){
						   continuecount++;
					   }else
					   { if(continuecount==2&&!findChess(x,yIndex1)&&!findChess(x+3,yIndex1)){
					    	if(geshu!=-1)
					    	{
					    		for(int h=0;h<=geshu;h++)
					    		{  
					    			if((kucun[h][0]==returnChess(x+1,yIndex1)&&kucun[h][1]==returnChess(x+2,yIndex1))||(kucun[h][1]==returnChess(x+1,yIndex1)&&kucun[h][0]==returnChess(x+2,yIndex1)))
					    			cunzai=1;
					    			else
					    				cunzai=0;
					    		}
					    	}
					    	if(cunzai==1)
					    	{break;}
					    	else
					    	{geshu++;
					    	kucun[geshu][0]=returnChess(x+1,yIndex1);
					    	kucun[geshu][1]=returnChess(x+2,yIndex1);
					    	poi=poi+fenshu;
					    	break;}
					    }
					   } 
				   }
				 //横向向东寻找
				   for(int x=xIndex1+1;x<=COLS;x++){
					  
					   if(getChess(x,yIndex1,c)!=null){
						   continuecount++;
					   }else
					   { if(continuecount==2&&!findChess(x,yIndex1)&&!findChess(x-3,yIndex1)){
						   if(geshu!=-1)
					    	{
					    		for(int h=0;h<=geshu;h++)
					    		{
					    			if((kucun[h][0]==returnChess(x-1,yIndex1)&&kucun[h][1]==returnChess(x-2,yIndex1))||(kucun[h][1]==returnChess(x-1,yIndex1)&&kucun[h][0]==returnChess(x-2,yIndex1)))
					    			cunzai=1;
					    			else
					    				cunzai=0;
					    		}
					    	}
					    	if(cunzai==1)
					    	{break;}
					    	else
						   {poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x-1,yIndex1);
					    	kucun[geshu][1]=returnChess(x-2,yIndex1);
					    	break;}
					    }
					   } 
				   }
				   continuecount=1;
				 //向北寻找
				   for(int y=yIndex1-1;y>=0;y--){
					   
					   if(getChess(xIndex1,y,c)!=null){
						   continuecount++;
					   }else
					   { if(continuecount==2&&!findChess(xIndex1,y)&&!findChess(xIndex1,y+3)){
						   if(geshu!=-1)
					    	{
					    		for(int h=0;h<=geshu;h++)
					    		{
					    			if((kucun[h][0]==returnChess(xIndex1,y+1)&&kucun[h][1]==returnChess(xIndex1,y+2))||(kucun[h][1]==returnChess(xIndex1,y+1)&&kucun[h][0]==returnChess(xIndex1,y+2)))
					    			cunzai=1;
					    			else
					    				cunzai=0;
					    		}
					    	}
					    	if(cunzai==1)
					    	{break;}
					    	else
						   {poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(xIndex1,y+1);
					    	kucun[geshu][1]=returnChess(xIndex1,y+2);
					    	break;}
					    }
					   } 
				   }
				 //向南寻找
				   for(int y=yIndex1+1;y<=ROWS;y++){
					
					   if(getChess(xIndex1,y,c)!=null){
						   continuecount++;
					   }else
					   { if(continuecount==2&&!findChess(xIndex1,y)&&!findChess(xIndex1,y-3)){
						   if(geshu!=-1)
					    	{
					    		for(int h=0;h<=geshu;h++)
					    		{
					    			if((kucun[h][0]==returnChess(xIndex1,y-1)&&kucun[h][1]==returnChess(xIndex1,y-2))||(kucun[h][1]==returnChess(xIndex1,y-1)&&kucun[h][0]==returnChess(xIndex1,y-2)))
					    			cunzai=1;
					    			else
					    				cunzai=0;
					    		}
					    	}
					    	if(cunzai==1)
					    	{break;}
					    	else
						   {poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(xIndex1,y-1);
					    	kucun[geshu][1]=returnChess(xIndex1,y-2);
					    	break;}
					    }
					   } 
				   }
				   continuecount=1;
				   //东北寻找
				    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
				 	  
				 	   if(getChess(x,y,c)!=null){
				 		   continuecount++;
				 	   }
				 	   else  { if(continuecount==2&&!findChess(x,y)&&!findChess(x-3,y+3)){
				 		  if(geshu!=-1)
					    	{
					    		for(int h=0;h<=geshu;h++)
					    		{
					    			if((kucun[h][0]==returnChess(x-1,y+1)&&kucun[h][1]==returnChess(x-2,y+2))||(kucun[h][1]==returnChess(x-1,y+1)&&kucun[h][0]==returnChess(x-2,y+2)))
					    			cunzai=1;
					    			else
					    				cunzai=0;
					    		}
					    	}
					    	if(cunzai==1)
					    	{break;}
					    	else
				 		   {poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x-1,y+1);
					    	kucun[geshu][1]=returnChess(x-2,y+2);
					    	break;}
					    }
				    	  } 
				    }
				    //西南寻找
				    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
				 	  
				 	   if(getChess(x,y,c)!=null){
				 		   continuecount++;
				 	   }
				 	   else  { if(continuecount==2&&!findChess(x,y)&&!findChess(x+3,y-3)){
				 		  if(geshu!=-1)
					    	{
					    		for(int h=0;h<=geshu;h++)
					    		{
					    			if((kucun[h][0]==returnChess(x+1,y-1)&&kucun[h][1]==returnChess(x+2,y-2))||(kucun[h][1]==returnChess(x+1,y-1)&&kucun[h][0]==returnChess(x+2,y-2)))
					    			cunzai=1;
					    			else
					    				cunzai=0;
					    		}
					    	}
					    	if(cunzai==1)
					    	{break;}
					    	else
				 		   {poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x+1,y-1);
					    	kucun[geshu][1]=returnChess(x+2,y-2);
					    	break;}
					    }
				    	  } 
				    }
				    continuecount=1;
				    //东南寻找
				    for(int x=xIndex1+1,y=yIndex1+1;y<=ROWS&&x<=COLS;x++,y++){
				 	   
				 	   if(getChess(x,y,c)!=null){
				 		   continuecount++;
				 	   }
				 	   else  { if(continuecount==2&&!findChess(x,y)&&!findChess(x-3,y-3)){
				 		  if(geshu!=-1)
					    	{
					    		for(int h=0;h<=geshu;h++)
					    		{
					    			if((kucun[h][0]==returnChess(x-1,y-1)&&kucun[h][1]==returnChess(x-2,y-2))||(kucun[h][1]==returnChess(x-1,y-1)&&kucun[h][0]==returnChess(x-2,y-2)))
					    			cunzai=1;
					    			else
					    				cunzai=0;
					    		}
					    	}
					    	if(cunzai==1)
					    	{break;}
					    	else
				 		   {poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x-1,y-1);
					    	kucun[geshu][1]=returnChess(x-2,y-2);
					    	break;}
					    }
				    	  } 
				    }
				    //西北寻找
				    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
				 	   
				 	   if(getChess(x,y,c)!=null){
				 		   continuecount++;
				 	   }
				 	   else  { if(continuecount==2&&!findChess(x,y)&&!findChess(x+3,y+3)){
				 		  if(geshu!=-1)
					    	{
					    		for(int h=0;h<=geshu;h++)
					    		{
					    			if((kucun[h][0]==returnChess(x+1,y+1)&&kucun[h][1]==returnChess(x+2,y+2))||(kucun[h][1]==returnChess(x+1,y+1)&&kucun[h][0]==returnChess(x+2,y+2)))
					    			cunzai=1;
					    			else
					    				cunzai=0;
					    		}
					    	}
					    	if(cunzai==1)
					    	{break;}
					    	else
				 		   {poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x+1,y+1);
					    	kucun[geshu][1]=returnChess(x+2,y+2);
					    	break;}
					    }
				    	  } 
				    }
				    continuecount=1;
				   
				   
			 }
		 }
	 }
	 
	 return poi;
 }
  
 public int huo3(int blackflag)
 {   int poi=0;Color c= new Color(0,0,0);int fenshu;
	 if(blackflag==1)
		 {c=Color.black;
		   fenshu=10;
		 }
	 else
		 {c=Color.white;
		  fenshu=-10;
		 }
	 {   int kucun[][]=new int[100][2];
	     int geshu=-1;int cunzai = 0;
		 for(int ll=0;ll<chessCount;ll++)
		 {  
	     if(geshu!=-1)
	     {
	    	 for(int i=0;i<=geshu;i++)
	    	 {
	    		 if(ll==kucun[geshu][0]||ll==kucun[geshu][1])
	    		 {cunzai=1;}
	    	 }
	     }
			 if(chessList[ll].getColor().equals(c)&&cunzai!=1)
			 {   
				 int continuecount=1;
				 int xIndex1,yIndex1;
				  xIndex1=chessList[ll].getX();
				  yIndex1=chessList[ll].getY();
				//横向向西寻找
				   for(int x=xIndex1-1;x>=0;x--){
					   
					   if(getChess(x,yIndex1,c)!=null){
						   continuecount++;
					   }else
					   { if(continuecount==2&&findChess(x,yIndex1)&&findChess(x+3,yIndex1)){
					    	poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x+1,yIndex1);
					    	kucun[geshu][1]=returnChess(x+2,yIndex1);
					    	break;
					    }
					   } 
				   }
				 //横向向东寻找
				   for(int x=xIndex1+1;x<=COLS;x++){
					  
					   if(getChess(x,yIndex1,c)!=null){
						   continuecount++;
					   }else
					   { if(continuecount==2&&findChess(x,yIndex1)&&findChess(x-3,yIndex1)){
					    	poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x-1,yIndex1);
					    	kucun[geshu][1]=returnChess(x-2,yIndex1);
					    	break;
					    }
					   } 
				   }
				   continuecount=1;
				 //向北寻找
				   for(int y=yIndex1-1;y>=0;y--){
					   
					   if(getChess(xIndex1,y,c)!=null){
						   continuecount++;
					   }else
					   { if(continuecount==2&&findChess(xIndex1,y)&&findChess(xIndex1,y+3)){
					    	poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(xIndex1,y+1);
					    	kucun[geshu][1]=returnChess(xIndex1,y+2);
					    	break;
					    }
					   } 
				   }
				 //向南寻找
				   for(int y=yIndex1+1;y<=ROWS;y++){
					
					   if(getChess(xIndex1,y,c)!=null){
						   continuecount++;
					   }else
					   { if(continuecount==2&&findChess(xIndex1,y)&&findChess(xIndex1,y-3)){
					    	poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(xIndex1,y-1);
					    	kucun[geshu][1]=returnChess(xIndex1,y-2);
					    	break;
					    }
					   } 
				   }
				   continuecount=1;
				   //东北寻找
				    for(int x=xIndex1+1,y=yIndex1-1;y>=0&&x<=COLS;x++,y--){
				 	  
				 	   if(getChess(x,y,c)!=null){
				 		   continuecount++;
				 	   }
				 	   else  { if(continuecount==2&&findChess(x,y)&&findChess(x-3,y+3)){
				 		  poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x-1,y+1);
					    	kucun[geshu][1]=returnChess(x-2,y+2);
					    	break;
					    }
				    	  } 
				    }
				    //西南寻找
				    for(int x=xIndex1-1,y=yIndex1+1;x>=0&&y<=ROWS;x--,y++){
				 	  
				 	   if(getChess(x,y,c)!=null){
				 		   continuecount++;
				 	   }
				 	   else  { if(continuecount==2&&findChess(x,y)&&findChess(x+3,y-3)){
				 		  poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x+1,y-1);
					    	kucun[geshu][1]=returnChess(x+2,y-2);
					    	break;
					    }
				    	  } 
				    }
				    continuecount=1;
				    //东南寻找
				    for(int x=xIndex1+1,y=yIndex1+1;y<=ROWS&&x<=COLS;x++,y++){
				 	   
				 	   if(getChess(x,y,c)!=null){
				 		   continuecount++;
				 	   }
				 	   else  { if(continuecount==2&&findChess(x,y)&&findChess(x+3,y+3)){
				 		  poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x+1,y+1);
					    	kucun[geshu][1]=returnChess(x+2,y+2);
					    	break;
					    }
				    	  } 
				    }
				    //西北寻找
				    for(int x=xIndex1-1,y=yIndex1-1;x>=0&&y>=0;x--,y--){
				 	   
				 	   if(getChess(x,y,c)!=null){
				 		   continuecount++;
				 	   }
				 	   else  { if(continuecount==2&&findChess(x,y)&&findChess(x-3,y-3)){
				 		  poi=poi+fenshu;
					    	geshu++;
					    	kucun[geshu][0]=returnChess(x-1,y-1);
					    	kucun[geshu][1]=returnChess(x-2,y-2);
					    	break;
					    }
				    	  } 
				    }
				    continuecount=1;
				 
				   
			 }
		 }
	 }
	 return poi;
 }
  
  
}



	 

	     
