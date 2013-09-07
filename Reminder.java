/**
 * @(#)Reminder.java
 *
 *
 * @author Zafar Ahmed Ansari zafar142003@iitj.ac.in
 * @version 1.00 2012/6/25
 */
import java.awt.event.*;
import javax.swing.plaf.basic.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
class NotesFile implements Serializable
{
	Date dateOfWriting=null;
	JTextField heading=new JTextField();
	JTextArea text=new JTextArea();
	NotesFile()
	{
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
	}
} 
class ReminderFile implements Serializable
{
	Date startDate=new Date();
	String msg;
	int repeatH=0, repeatM=0, repeatS=0;
	int repeatTimes=0;
	boolean isScheduled=false;
} 
class Task extends TimerTask
{
	//Date clock=new Date();//add time counter in dialog
	String message;
	Date date;
	public Task(String msg, Date date)
	{
		super();
		this.date=date;
		message=msg;
	}
	public void run()
	{
		char test[]=new char[2];
		int num, counter=0;
		String quote="";
		StringBuffer buffer=new StringBuffer();
		Random random=new Random();
		/*Date currentTime=new Date();
		long currentSeconds=60*60*currentTime.getHours()+60*currentTime.getMinutes()+currentTime.getSeconds();
		if(clock.getDay()!=currentTime.getDay())
			currentSeconds+=24*3600;	//In case the date changes to the next day's	
		long originalSeconds=60*60*clock.getHours()+60*clock.getMinutes()+clock.getSeconds();
		long elapsedTime=currentSeconds-originalSeconds;
		String time=""+(elapsedTime/3600)+":"+((elapsedTime-((int)(elapsedTime/3600))*3600)/60)+":"+((elapsedTime-((int)(elapsedTime/3600))*3600)-(((int)((elapsedTime-((int)(elapsedTime/3600))*3600)/60))*60));
		*/
		try{
			FileReader file=new FileReader("quotes.txt");
			num=(int)(random.nextFloat()*110.0);// approx. 50 quotes present
			//We have to find a new line pair. A single newline is represented in file by "\r\n"
			while(file.read(test)!=-1 && counter<num )
			{
				if(test[0]=='\r' && test[1]=='\n')
				{	
					if(file.read(test)!=-1 && (test[0]=='\r' && test[1]=='\n') )
						counter++;
				}
				if(test[1]=='\r')
				{	
					if(file.read(test)!=-1 && (test[0]=='\n' && test[1]=='\r') )
						counter++;
				}
			}	//System.out.println(test[0]+""+test[1]+num+" "+counter);
			buffer.append(test);
			counter=0;
			while(file.read(test)!=-1 && counter==0 )//find the next consecutive new line pair
			{
				if(test[0]=='\r' && test[1]=='\n')// a new line character
				{	
					if(file.read(test)!=-1 && (test[0]=='\r' && test[1]=='\n') )//another new line character
						counter=1;
					else
						buffer.append(test);
				}
				else
					buffer.append(test);
				if(test[1]=='\r')
				{
						if(file.read(test)!=-1 && (test[0]=='\n' && test[1]=='\r') )//another new line character
							counter=1;
					else
						buffer.append(test);
				}
					
			}
			quote=buffer.toString().trim();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		JDialog dialog=new JDialog();
		//TextArea text=new TextArea("Time to take a break!\n"+time +" hours since last logged in!"+"\n\n\t\t\tThought for the Hour\n\n"+quote, 15,60,TextArea.SCROLLBARS_NONE);
		TextArea text=new TextArea(message+"\n\n\t\t\tA Thought\n\n"+quote, 15,60,TextArea.SCROLLBARS_NONE);
		
		text.setEditable(false);
		dialog.add(text);
		dialog.setTitle("Reminder");
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setSize(400, 300);
		dialog.setAlwaysOnTop(true);
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
  		Dimension dim = toolkit.getScreenSize();
  	//	System.out.println("Width of Screen Size is "+dim.width+" pixels");
  	//	System.out.println("Height of Screen Size is "+dim.height+" pixels");
 
		dialog.setLocation(dim.width/2-200,dim.height/2-150);
		dialog.setVisible(true);
	//	repaint();
		int i=0;
		ReminderFile file=null;
						try{// For storing this reminder in file we open the file, read the only ArrayList object it holds, update it with our reminder and then write it back in place of the original ArrayList object.
    					 		ArrayList fileObjects=null;
    							FileInputStream fis = new FileInputStream("reminders");
				    		    ObjectInputStream ois = new ObjectInputStream(fis);
				    		    Object obj=null;
				    		    try{
				    		    
								if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						 }
				    		    }catch(EOFException ex)
    							{
    								
    							}
								if(fileObjects==null)
									fileObjects=new ArrayList();
								for(i=0;i<fileObjects.size();i++)
								{
										file=(ReminderFile)fileObjects.get(i);
										if(file.startDate.equals(date) && file.msg.equals(message))
										{
											System.out.println("Scheduled");
											file.isScheduled=true;
											break;
										}
								}
								fileObjects.remove(i);
								fileObjects.add(file);
								ois.close();
								fis.close();
    							FileOutputStream fos = new FileOutputStream("reminders", false);
				    		    ObjectOutputStream oos = new ObjectOutputStream(fos);
								
								oos.writeObject(fileObjects);
								oos.close();
								fos.close();
    							System.out.println("file written");
    						
    						}catch(Exception e)
    						{
    							e.printStackTrace();
    						}
	}
}
//After installation it will be minimised to the system tray. 
//On clicking the icon in the system tray a calendar and time since last logged in is shown.
// Also a tab to see reminders not yet seen is shown. 
//Once a calendar date is clicked a new reminder scheduling is started.
 
 //It asks the time of scheduling, the message and its frequency (x times with y interval).
// The application on startup checks whether there are any reminders to be shown by going through the list of reminders not yet seen.
// Add system tray menu(can also show the current number of hours logged in)
//Option to remove a reminder
// ask whether to start at startup (Windows/ Linux)
//	Show Calendar and schedule reminders (hourly ergonomic reminder, schedule reminder using calendar)
// Option to Use sound in reminding in the settings tab. 
//In reminder dialog have a space to store some update/progress (useful in repeated reminders) 
//Option to set alarm sound for each reminder
class MyFrame extends JFrame implements MouseListener , ActionListener//, ItemListener
{ // copied from JTabbedPane example on Oracle's site
	private JButton addNote=new JButton("Save note");
	private JButton deleteNote=new JButton("Delete note");
	private JButton editNote=new JButton("Edit");
	private JButton refreshNote=new JButton("Refresh"); 
	private JPanel notesPanel=new JPanel();
	private NotesFile note=new NotesFile();
	private NotesFile note2=new NotesFile();
	private JScrollPane scroll=null;
    private JCheckBox listNotes[]=null;
    private boolean editFlag =false;//to check if an edit is going on
	private Date clock=new Date();//add time counter in dialog
	private JTextArea noteText =new JTextArea("");
	private JTextField noteHeading=new JTextField();
	
	private JCheckBox list[];
	private ReminderFile file=new ReminderFile();
	private final int tabNumber = 4;
    private final JTabbedPane pane = new JTabbedPane();
   // private JMenuItem tabComponentsItem;
    //Task task=new Task();
    java.util.Timer newTimer =new java.util.Timer();
    private JPanel mainPanel=new JPanel();
    private JPanel calendar=new JPanel();//inside mainPanel
    private JPanel newReminderPanel=new JPanel();//inside mainPanel
    private JPanel reminderList=new JPanel();
    private JPanel settings=new JPanel();
    //private JPanel about=new JPanel();
    private GregorianCalendar currentCalendar;    
    //private JPanel calendar=null, reminderList=null, settings=null, about=null;
    private JMenuItem scrollLayoutItem;
    private JLabel[] calendarEntry=new JLabel[56];
    //private JLabel[] label=new JLabel[8];
    
    JTextField repeatH=new JTextField("0",2);
    JTextField repeatM=new JTextField("0",2);
    JTextField repeatS=new JTextField("0",2);
    		
    JTextField timeH=new JTextField("0",2);
    JTextField timeM=new JTextField("0",2);
    JTextField timeS=new JTextField("0",2);
    JTextField message=new JTextField(15);	
    JLabel error=new JLabel("");
    JTextArea missedReminder=new JTextArea();
    JTextArea text;
    private JComboBox selectRepeat=null;
    private JButton okay=null;
    private JButton delete=new JButton("Delete");
    private JButton refresh=new JButton("Refresh");
    int selectedDate=0;//When the user selects a date in the calendar
    public MyFrame(String title) {
        super(title);
        System.out.println("Constructor"+file.isScheduled);
        
        missedReminder.setEditable(false);
        //schedule pending reminders
        schedulePending();
        
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
        refreshNote.addActionListener(this);
        deleteNote.addActionListener(this);
        editNote.addActionListener(this);
        addNote.addActionListener(this);
        noteHeading.setColumns(15);
        
        refresh.addActionListener(this);
        delete.addActionListener(this);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //initMenu();        
        add(pane);        
        GregorianCalendar c=new GregorianCalendar();	
       prepareCalendar(c);
       System.gc();
        
        
        
        // reminderList
        prepareReminderList();
        System.gc();
        //In order to delete existing reminders we have to save the Task objects that have been scheduled till now.
        //notes panel
         initialiseNotes();
        
        //about
        initialiseAbout();
        
    }
    void initialiseNotes()
    {
    	//note.heading.setColumns(20);
    	notesPanel.removeAll();
    	JLabel heading=new JLabel("Title ");
    	JPanel panel1=new JPanel();
    	//note.heading.setAlignmentX(0.0f);
    	heading.setAlignmentX(0.0f);
    	panel1.add(heading);
    	panel1.add(noteHeading);
    	
    	panel1.setAlignmentX(0.0f);
    	
    	notesPanel.add(panel1);
    	JLabel label=new JLabel("Note");
    	label.setAlignmentX(0.0f);
    	notesPanel.add(label);
    //	note.text.setAlignmentX(0.0f);
    	JScrollPane paneScroll=new JScrollPane();
    	paneScroll.setPreferredSize(new Dimension(600,300));
    	paneScroll.getViewport().add(noteText);
    	paneScroll.setAlignmentX(0.0f);
    	
    	notesPanel.add(paneScroll);
    	addNote.setAlignmentX(0.0f);
    	notesPanel.add(addNote);
    	
    	JLabel lab=new JLabel("Stored notes:");
    	lab.setAlignmentX(0.0f);
    	notesPanel.add(lab);
    	
    	
    	//JLabel text=new JLabel("Text");
    	scroll = new JScrollPane();
        scroll.setAlignmentX(0.0f);
		ArrayList fileObjects=new ArrayList();;
		FileInputStream fis=null;
		ObjectInputStream ois =null;
		String detail=null;
		reminderList.setLayout(new BoxLayout(reminderList, BoxLayout.Y_AXIS ));
		
		//ReminderFile file=new ReminderFile();
			try{
				//FileOutputStream fos = new FileOutputStream("reminders");//to create this file
				//ObjectOutputStream oos=new ObjectOutputStream(fos);
				
				//oos.writeObject(fileObjects);
				//oos.close();
				//fos.close();		    		   
				Object obj=null;	
    			fis = new FileInputStream("notes");
				ois = new ObjectInputStream(fis);
				//System.out.println(ois.available());
				if((obj = ois.readObject()) != null) {
                
                if (obj instanceof ArrayList) {
                
                    //System.out.println(((ArrayList)obj).get(0));
                    fileObjects=(ArrayList)obj;
                }
                
            }
					fileObjects=(ArrayList)ois.readObject();				
    			//FileInputStream fos = new FileInputStream("reminders");				
				ois.close();
				fis.close();
    			System.out.println("fileObjects "+fileObjects);
			}
    		catch (EOFException ex) { //This exception will be caught when EOF is reached
            System.out.println("End of file reached.");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
          	try{
          		  FileOutputStream fos = new FileOutputStream("notes");//create file for the first time
          		  ObjectOutputStream oos=new ObjectOutputStream(fos);
          		  oos.close();
          	  	  fos.close();
          	}catch(Exception e)
          	{
          	}
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the ObjectInputStream
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        JPanel pane=new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        ArrayList arr=new ArrayList();
        scroll.setPreferredSize(new Dimension(600,100));
			if(!fileObjects.isEmpty())
			{
				System.out.println("size "+fileObjects.size());
				listNotes=new JCheckBox[fileObjects.size()];
				
				for(int  i=0;i<fileObjects.size();i++)
				{
					note=(NotesFile)fileObjects.get(i);
					if(note.text.getText().length()<15)
				
					System.out.println(note.text.getText());
					detail="Date: "+note.dateOfWriting+", Heading: "+note.heading.getText()+", Text: "+note.text.getText()+" ..."; 
					listNotes[i]=new JCheckBox(detail);
					listNotes[i].setMnemonic(KeyEvent.VK_C);
					pane.add(listNotes[i]);
					//list[i].addItemListener(this);
					//reminderList.add(list[i]);
				//	 arr.add(listNotes[i]);
 	
				}
				
    	
 	   }
 	   else
			{
				notesPanel.add(new JLabel("No notes!"));
			}
		//Vector v=new Vector(arr);
		//JList items = new JList(listNotes);
		//items.setVisibleRowCount(3);
 	   scroll.setAlignmentX(0.0f);
 	   editNote.setAlignmentX(0.0f);
 	   deleteNote.setAlignmentX(0.0f);
 	   refreshNote.setAlignmentX(0.0f);
 	   scroll.getViewport().setView(pane);
 	   notesPanel.add(scroll);
 	   
 	   JPanel p=new JPanel();
 	   
 	   
 	   p.add(editNote);
 	   p.add(deleteNote);
 	   p.add(refreshNote);
 	   p.setAlignmentX(0.0f);
 	   notesPanel.add(p);
    }    void initialiseAbout()
    {
    	text=new JTextArea();
    	text.setEditable(false);
    	text.setLineWrap(true);
    	text.setWrapStyleWord(true);
    	text.setText("A Complete Reminder and Notes System v0.9\nCreated by\nZafar Ahmed Ansari\nzafar142003@iitj.ac.in\nUtilities:\nSet up reminders of every kind- whether they be reminders that repeat for a fixed number of times, reminders which repeat infintely many times, or simple one-time reminders. Add notes at any time, edit them later, remove them or keep them stored for later use.\n\t*Each reminder dialog window also shows a stimulating random quote from history's greatest people!\n\t*Add notes on the 'Notes' tab.\n\t*Remove scheduled reminders on the 'Scheduled Reminders' tab.\n\t*Hit refresh button to see the latest update.\n\t*Get notifications about missed one-time reminders in the 'Scheduled Reminders' tab.\n\t*Get total time since last login in the 'Scheduled Reminders' tab.\nThe program minimises to the system tray after first run.\nRight click on the system tray icon to open or exit the application.\nFor best use add the program to the startup directory of your computer.\nThe program remembers all reminders set and schedules all past unmet reminders on every run.\nAll the input time fields are in 24 hour format.\nFlip calendar month by arrow buttons given on either sides of the month name.\nA minimum JRE 1.7.0_02 is required to run.");
    	
    }
    void schedulePending()
    {
    	int j=0,i=0;
    	try{
    							ArrayList fileObjects=null;
    							FileInputStream fis = new FileInputStream("reminders");
				    		    ObjectInputStream ois = new ObjectInputStream(fis);
								Object obj=null;
								System.out.println("pending"+file.isScheduled);
								try{
									if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						}
								}catch(EOFException ex)
								{
								 return;	
								}
								if(fileObjects==null)
									fileObjects=new ArrayList();
								
								ois.close();
								fis.close();
								while(!fileObjects.isEmpty())
								{
									file=(ReminderFile)fileObjects.remove(0);
									//j--;
									System.out.println("isEmpty"+file.isScheduled);
									//j++;
									if(file.repeatTimes==0)
									{
										if(file.startDate.after(new Date()))
										{	newTimer.schedule(new Task(file.msg, file.startDate),file.startDate);
											//file.isScheduled=true;
										}
										else
										{
											//reminder missed, so show notification in remiderList panel
											if(file.isScheduled==false)
												missedReminder.setText(missedReminder.getText()+"\n\rMissed one-time reminder "+file.startDate+" message='"+file.msg+"'");
											
										}	
    			
									}
									else
										if(file.repeatTimes==-1)
										{//Indefinite reminder
											GregorianCalendar c=new GregorianCalendar(file.startDate.getYear()+1900, file.startDate.getMonth(),file.startDate.getDate(),file.startDate.getHours(),file.startDate.getMinutes(),file.startDate.getSeconds());
	        								
										while(c.before(new GregorianCalendar()))
										{
											c.add(c.SECOND,file.repeatS);
	        								c.add(c.MINUTE,file.repeatM);
	        								c.add(c.HOUR,file.repeatH);
	       										
										}
											newTimer.scheduleAtFixedRate(new Task(file.msg,file.startDate),c.getTime(), 1000*(file.repeatH*3600+file.repeatM*60+file.repeatS));
    										
										}
										else
										{//a reminder to go off fixed number of times
											GregorianCalendar c=new GregorianCalendar(file.startDate.getYear()+1900, file.startDate.getMonth(),file.startDate.getDate(),file.startDate.getHours(),file.startDate.getMinutes(),file.startDate.getSeconds());
	        								i=1;
											while(c.before(new GregorianCalendar()) && i<=file.repeatTimes) 
											{	
											//	if(file.isScheduled==false)
												//	missedReminder.setText(missedReminder.getText()+"\nMissed fixed-time reminder "+c.getTime()+" message='"+file.msg+"'");
												//System.out.println("missed at "+c);
												c.add(c.SECOND,file.repeatS);
	        									c.add(c.MINUTE,file.repeatM);
	        									c.add(c.HOUR,file.repeatH);
	       										i++;
											}
											while(i<=file.repeatTimes)
											{
												newTimer.schedule(new Task(file.msg,file.startDate),c.getTime());
												//file.isScheduled=true;
												c.add(c.SECOND,file.repeatS);
	        									c.add(c.MINUTE,file.repeatM);
	        									c.add(c.HOUR,file.repeatH);
	       										i++;
											}
										}
								}
    							
    		}catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    }
    void prepareReminderList()
    {
    	reminderList.removeAll();
    	Date currentTime=new Date();
    	int i=0;
		long currentSeconds=60*60*currentTime.getHours()+60*currentTime.getMinutes()+currentTime.getSeconds();
		if(clock.getDay()!=currentTime.getDay())
			currentSeconds+=24*3600;	//In case the date changes to the next day's	
		long originalSeconds=60*60*clock.getHours()+60*clock.getMinutes()+clock.getSeconds();
		long elapsedTime=currentSeconds-originalSeconds;
		JLabel logCount=new JLabel();
		String time=""+(elapsedTime/3600)+":"+((elapsedTime-((int)(elapsedTime/3600))*3600)/60)+":"+((elapsedTime-((int)(elapsedTime/3600))*3600)-(((int)((elapsedTime-((int)(elapsedTime/3600))*3600)/60))*60));
		//String text=logCount.getText();
		//if(text.length()>28 && text.substring(0,1).equals("T"))
		//	text=text.substring(29);
		//	text.trim();
		//System.out.print(text.length());
		
		logCount.setText("Time since last login: "+time);
		logCount.setAlignmentX(0.0f);
		missedReminder.setAlignmentX(0.0f);
		//ReminderFile reminderObjectList[]=new ReminderFile();
		ArrayList fileObjects=new ArrayList();;
		FileInputStream fis=null;
		ObjectInputStream ois =null;
		String detail=null;
		reminderList.setLayout(new BoxLayout(reminderList, BoxLayout.Y_AXIS ));
		
		//ReminderFile file=new ReminderFile();
			try{
				//FileOutputStream fos = new FileOutputStream("reminders");//to create this file
				//ObjectOutputStream oos=new ObjectOutputStream(fos);
				
				//oos.writeObject(fileObjects);
				//oos.close();
				//fos.close();		    		   
				Object obj=null;	
    			fis = new FileInputStream("reminders");
				ois = new ObjectInputStream(fis);
				//System.out.println(ois.available());
				if((obj = ois.readObject()) != null) {
                
                if (obj instanceof ArrayList) {
                
                    //System.out.println(((ArrayList)obj).get(0));
                    fileObjects=(ArrayList)obj;
                }
                
            }
					fileObjects=(ArrayList)ois.readObject();				
    			//FileInputStream fos = new FileInputStream("reminders");				
				ois.close();
				fis.close();
    			System.out.println("fileObjects "+fileObjects);
			}
    		catch (EOFException ex) { //This exception will be caught when EOF is reached
            System.out.println("End of file reached.");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
          	try{
          		  FileOutputStream fos = new FileOutputStream("reminders");//create file for the first time
          		  ObjectOutputStream oos=new ObjectOutputStream(fos);
          		  oos.close();
          	  	  fos.close();
          	}catch(Exception e)
          	{
          	}
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the ObjectInputStream
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setAlignmentX(0.0f);
        scrollPane.setPreferredSize(new Dimension(600,100));
		JPanel pane=new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
			
			if(!fileObjects.isEmpty())
			{
				System.out.println("size "+fileObjects.size());
				list=new JCheckBox[fileObjects.size()];
				for( i=0;i<fileObjects.size();i++)
				{
					file=(ReminderFile)fileObjects.get(i);
					detail="starting "+file.startDate.toString()+" to repeat "+file.repeatTimes+" times, with period "+file.repeatH+":"+file.repeatM+":"+file.repeatS+" and message='"+file.msg+"'"; 
					list[i]=new JCheckBox(detail);
					list[i].setMnemonic(KeyEvent.VK_C);
					//list[i].addItemListener(this);
					//reminderList.add(list[i]);
					 pane.add(list[i]);
 	
				}
	//	JCheckBox[] list=new JCheckBox[];
	//	 = new JCheckBox("Chin");
	  //  chinButton.setMnemonic(KeyEvent.VK_C); 
   // 	chinButton.setSelected(true);
			}else
			{
				reminderList.add(new JLabel("No reminders!"));
			}
			scrollPane.getViewport().add(pane);

		reminderList.add(scrollPane);
		JPanel p=new JPanel();
		p.setAlignmentX(0.0f);
		p.add(delete);
		p.add(refresh);
		reminderList.add(p);
	
		
		reminderList.add(logCount);
		reminderList.add(missedReminder);
		//delete.addActionListener(this);
		
    }
  /*  public void itemStateChanged(ItemEvent e) 
    {
    	Object source = e.getItemSelectable();
		for(int i=0;i<list.length;i++)
			
			if(list[i].equals(source))
				
    	
    }*/
    void prepareCalendar(GregorianCalendar cal)
    {
    	currentCalendar=cal;
    	 //java.util.GregorianCalendar cal=new java.util.GregorianCalendar();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.removeAll();
        /*calendar=new JPanel();
        reminderList=new JPanel();
        settings=new JPanel();
        about=new JPanel();
        */
        //setting calendar
        calendar.removeAll();
        calendar.setLayout(new GridLayout(0,7));
        
         calendarEntry[0]=new JLabel("", JLabel.CENTER);
        calendarEntry[0].setText("<html><u>"+"&laquo;"+"</u></html>");//underline <
      
        
       // calendarEntry[0]=new JLabel("", JLabel.CENTER);
        //calendarEntry[0].setText("<html><u><</u></html>");
        Date date=cal.getTime();
        int year=date.getYear();
        int month=date.getMonth();
        int currentDate=date.getDate();
        year+=1900;
        calendarEntry[1]=new JLabel(" ");
        
        calendarEntry[2]=new JLabel(" ");
    
        switch(month)
        {
        	case 0:
        		calendarEntry[3]=new JLabel("Jan "+year, JLabel.CENTER);
        		break;
        	case 1:
        		calendarEntry[3]=new JLabel("Feb "+year, JLabel.CENTER);
        		break;
        	case 2:
        		calendarEntry[3]=new JLabel("Mar "+year, JLabel.CENTER);
        		break;
        	case 3:
        		calendarEntry[3]=new JLabel("Apr "+year, JLabel.CENTER);
        		break;
        	case 4:
        		calendarEntry[3]=new JLabel("May "+year, JLabel.CENTER);
        		break;
        	case 5:
        		calendarEntry[3]=new JLabel("Jun "+year, JLabel.CENTER);
        		break;
        	case 6:
        		calendarEntry[3]=new JLabel("Jul "+year, JLabel.CENTER);
        		break;
        	case 7:
        		calendarEntry[3]=new JLabel("Aug "+year, JLabel.CENTER);
        		break;
        	case 8:
        		calendarEntry[3]=new JLabel("Sep "+year, JLabel.CENTER);
        		break;
        	case 9:
        		calendarEntry[3]=new JLabel("Oct "+year, JLabel.CENTER);
        		break;
        	case 10:
        		calendarEntry[3]=new JLabel("Nov "+year, JLabel.CENTER);
        		break;
        	case 11:
        		calendarEntry[3]=new JLabel("Dec "+year, JLabel.CENTER);
        		break;
        	
        }
      /*  calendarEntry[3].setBorder(BorderFactory.createCompoundBorder(
             BorderFactory.createLineBorder(Color.black),
             BorderFactory.createEmptyBorder(5,5,5,5)
        ));
       */
        calendarEntry[3].setText("<html><u>"+calendarEntry[3].getText()+"</u></html>");
        calendarEntry[4]=new JLabel(" ");
        calendarEntry[5]=new JLabel(" ");
        calendarEntry[6]=new JLabel("", JLabel.CENTER);
        calendarEntry[6].setText("<html><u>&raquo;</u></html>");//underline >
      
       
        calendarEntry[7]=new JLabel("Sun", JLabel.CENTER);
        calendarEntry[8]=new JLabel("Mon", JLabel.CENTER);
        calendarEntry[9]=new JLabel("Tue", JLabel.CENTER);
        calendarEntry[10]=new JLabel("Wed", JLabel.CENTER);
        calendarEntry[11]=new JLabel("Thur", JLabel.CENTER);
        calendarEntry[12]=new JLabel("Fri", JLabel.CENTER);
        calendarEntry[13]=new JLabel("Sat", JLabel.CENTER);
        date=new Date(year-1900,month,1);
        int k=0,j=0,i=1,day;
        day=date.getDay();
        j=14+day;
        i=14;
        for (k=0;k<day;k++,i++)
        	calendarEntry[i]=new JLabel(" ");
        if(month==11)
        	k=31;//For December number of days in the month are 31
        else
        {
        
	        GregorianCalendar c=new GregorianCalendar(year-1900, month+1,1);
	        c.add(c.DAY_OF_MONTH,-1);
	        k=c.get(c.DATE);
        }
        for(i=1;i<=k;i++,j++)
	        calendarEntry[j]= new JLabel(""+i, JLabel.CENTER);
	        
        //Add listeners to the above JLabels
        calendarEntry[0].addMouseListener(this);
        
        calendarEntry[6].addMouseListener(this);
     //   for(i=7;i<=13;i++)
     //   	calendar.add(calendarEntry[i]);
      //  for (k=0;k<day;k++,i++)
      //  	calendar.add(calendarEntry[i]);
        j=14+day;
        //System.out.println(day);
		for(i=1;i<=k;i++,j++)
		{	calendarEntry[j].addMouseListener(this);
			//calendar.add(calendarEntry[j]);
        }
        for(i=0;i<(14+day+k);i++)
        	calendar.add(calendarEntry[i]);
        mainPanel.add(calendar,BorderLayout.WEST);
        mainPanel.add(new JLabel("Choose a date to set new reminder.", JLabel.CENTER),BorderLayout.NORTH);
        
        
        //mainPanel.add(new JLabel("Hihihihihi"), BorderLayout.EAST);// add reminder details
        System.gc();
        repaint();
        
    }
    void advanceMonth()
    {
    	
    		currentCalendar.add(currentCalendar.MONTH,1);
	        System.gc();
	        prepareCalendar(currentCalendar);//flip back a month
    		
    }
    void reverseMonth()
    {
    		currentCalendar.add(currentCalendar.MONTH,-1);
	        System.gc();
	        prepareCalendar(currentCalendar);//flip back a month
    		
    }
    void makeNewReminderPanel()
    {
    		newReminderPanel.removeAll();
    		JLabel startTime=new JLabel("Start time:",JLabel.LEFT);
    		startTime.setBorder(BorderFactory.createCompoundBorder(
             BorderFactory.createLineBorder(Color.black),
             BorderFactory.createEmptyBorder(5,5,5,5)
        ));

    		JLabel hours=new JLabel("H ",JLabel.LEFT);
    		JLabel minutes=new JLabel("M ",JLabel.LEFT);
    		JLabel seconds=new JLabel("S ",JLabel.LEFT);
    		JLabel repeatLabel=new JLabel("Repeat ",JLabel.LEFT);
    		JLabel interval=new JLabel("Repeat every:",JLabel.CENTER);
    		interval.setBorder(BorderFactory.createCompoundBorder(
             BorderFactory.createLineBorder(Color.black),
             BorderFactory.createEmptyBorder(5,5,5,5)
        ));

    		JLabel hours2=new JLabel("H ",JLabel.LEFT);
    		JLabel minutes2=new JLabel("M ",JLabel.LEFT);
    		JLabel seconds2=new JLabel("S ",JLabel.LEFT);
    		
    		JLabel messageLabel=new JLabel("Message ");
    		messageLabel.setBorder(BorderFactory.createCompoundBorder(
             BorderFactory.createLineBorder(Color.black),
             BorderFactory.createEmptyBorder(5,5,5,5)
        ));

    		newReminderPanel.setLayout(new BoxLayout(newReminderPanel,BoxLayout.Y_AXIS));
    	 	JLabel date=new JLabel(""+selectedDate+"/"+(currentCalendar.get(GregorianCalendar.MONTH)+1)+"/"+(currentCalendar.get(GregorianCalendar.YEAR)));
    	 	String repeat[]={
    	 					"0 times","indefinitely"    	 					
    	 					};
    	 	
    	 	okay=new JButton("OK"); 
    	 	selectRepeat=new JComboBox(repeat);
    	 	selectRepeat.setEditable(true);
    	 	//selectRepeat.addActionListener(this);
    	 	//newReminderPanel.add(startTime);
    	 /*	date.setAlignmentX(0.0f);
    	 	startTime.setAlignmentX(0.0f);
    	 	hours.setAlignmentX(0.0f);
    		timeH.setAlignmentX(0.0f);
    		minutes.setAlignmentX(0.0f);
    		timeM.setAlignmentX(0.0f);
    		seconds.setAlignmentX(0.0f);
    		timeS.setAlignmentX(0.0f);
    		
    	 	repeatLabel.setAlignmentX(0.0f);
    	 	selectRepeat.setAlignmentX(0.0f);
    	 	
    	 	interval.setAlignmentX(0.0f);
    	 	messageLabel.setAlignmentX(0.0f);
    	 	hours2.setAlignmentX(0.0f);
    	 	repeatH.setAlignmentX(0.0f);
    	 	minutes2.setAlignmentX(0.0f);
    	 	repeatM.setAlignmentX(0.0f);
    	 	seconds2.setAlignmentX(0.0f);
    	 	repeatS.setAlignmentX(0.0f);
    	 */	
    	 	newReminderPanel.add(date);
    	 	JPanel p=new JPanel();
    	 	p.add(startTime);
    	 	p.add(hours);
    	 	p.add(timeH);
    	 	p.add(minutes);
    	 	p.add(timeM);
    	 	p.add(seconds);
    	 	p.add(timeS);
    	 	
    	 	newReminderPanel.add(p);
    		
    		p=new JPanel();
    		p.add(repeatLabel);
    		p.add(selectRepeat);
    		newReminderPanel.add(p);
    		
    		//newReminderPanel.add(interval);
    		p=new JPanel();
    		
    		p.add(interval);
    		p.add(hours2);
    	 	p.add(repeatH);
    		p.add(minutes2);
    	 	p.add(repeatM);
    	 	p.add(seconds2);
    	 	p.add(repeatS);
    	 	
    		newReminderPanel.add(p);
    		p=new JPanel();
    		p.add(messageLabel);
    		p.add(message);
			newReminderPanel.add(p);
			okay.addActionListener(this);
			newReminderPanel.add(okay);
			mainPanel.add(newReminderPanel, BorderLayout.EAST);
    		repaint();
    }
    
    public void actionPerformed(ActionEvent ae)
    {      
    	//System.out.print(timeH.getText()+"In action performed");
    	if(ae.getSource().equals(deleteNote) )
    	{
    			if(listNotes==null || listNotes.length==0)
    								return;//no notes to delete
    							System.out.println("In delete");
    							ArrayList fileObjects=null;
    							try{
    								FileInputStream fis = new FileInputStream("notes");
				    		    	ObjectInputStream ois = new ObjectInputStream(fis);
				    		    	Object obj=null;
				    		    	try{
				    		    
									if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						 }
				    		    }catch(EOFException ex)
    							{
    								
    							}
								if(fileObjects==null)
									fileObjects=new ArrayList();
								if(fileObjects.isEmpty())
									return;
								for(int i=0,j=0;i<listNotes.length;i++,j++)
    							{
    								if(listNotes[i].isSelected())
    								{	
    									fileObjects.remove(j);
    									j--;
    								}	
    							}
    		
								ois.close();
								fis.close();
    							FileOutputStream fos = new FileOutputStream("notes", false);
				    		    ObjectOutputStream oos = new ObjectOutputStream(fos);
								
								oos.writeObject(fileObjects);
								oos.close();
								fos.close();
    							System.out.println("file written");
    						
    						}
    						catch(Exception e)
    						{
    								//error.setText("Could not write reminder!");
    								e.printStackTrace();
    								try{
    									//ois.close();
    									
									//	fis.close();
										
    								}
    								catch(Exception ea)
    								{
    									ea.printStackTrace();
    								}
    								
    						}
    		
    		return;
    	
    	}
    	if(ae.getSource().equals(editNote))
    	{
    							editFlag=true;
    							addNote.setText("Save edited");
    							if(listNotes==null || listNotes.length==0 )
    								return;
    							System.out.println("In edit");
    							ArrayList fileObjects=null;
    							try{
    								FileInputStream fis = new FileInputStream("notes");
				    		    	ObjectInputStream ois = new ObjectInputStream(fis);
				    		    	Object obj=null;
				    		    	try{
				    		    
									if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						 }
				    		    }catch(EOFException ex)
    							{
    								
    							}
								if(fileObjects==null)
									fileObjects=new ArrayList();
								if(fileObjects.isEmpty())
									return;
								int j=0,k=0;
								for(int i=0;i<listNotes.length;i++)
    							{
    								if(listNotes[i].isSelected())
    								{	
    									j++;
    									if(j==1)
    										k=i;
    								}	
    							}
    							ois.close();
								fis.close();
    							if(j!=1)
    								return;
								
								if(j==1)
								{
								
								   System.out.print("k="+k);
									note2=(NotesFile)fileObjects.get(k);
    							}
    							System.out.println(note2.heading.getText()+" "+note2.text.getText());
    							noteHeading.setText(note2.heading.getText());
    							noteText.setText(note2.text.getText());
    							
    						/*	FileOutputStream fos = new FileOutputStream("notes", false);
				    		    ObjectOutputStream oos = new ObjectOutputStream(fos);
								
								oos.writeObject(fileObjects);
								oos.close();
								fos.close();
    							System.out.println("file written");
    						*/
    						}
    						catch(Exception e)
    						{
    								//error.setText("Could not write reminder!");
    								e.printStackTrace();
    								try{
    									//ois.close();
    									
									//	fis.close();
										
    								}
    								catch(Exception ea)
    								{
    									ea.printStackTrace();
    								}
    								
    						}
    		
    		return;
    	}
    	if(ae.getSource().equals(refreshNote))
    	{
    		initialiseNotes();
    		repaint();
    		System.gc();
    		return;
    	}
    	if(ae.getSource().equals(addNote))
    	{
    		note.dateOfWriting=new Date();
    		if(editFlag==true)
    		{
    			
    			//	if(listNotes==null || listNotes.length==0)
    			//					return;//no notes to delete
    							System.out.println("In editing");
    							ArrayList fileObjects=null;
    							try{
    								FileInputStream fis = new FileInputStream("notes");
				    		    	ObjectInputStream ois = new ObjectInputStream(fis);
				    		    	Object obj=null;
				    		    	try{
				    		    
									if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						 }
				    		    }catch(EOFException ex)
    							{
    								
    							}
								if(fileObjects==null)
									fileObjects=new ArrayList();
							//	if(fileObjects.isEmpty())
							//		return;
								System.out.println(fileObjects.size()+" "+note2+" "+note);
								for(int i=0,j=0;i<fileObjects.size();i++,j++)
    							{
    								note=(NotesFile)fileObjects.get(i);
    								if(note.dateOfWriting.equals(note2.dateOfWriting) && note.heading.getText().equals(note2.heading.getText()) && note.text.getText().equals(note2.text.getText()))
    								{	
    									fileObjects.remove(i);
    									System.out.println("removed");
    									break;
    								}	
    							}
    		
								ois.close();
								fis.close();
    							FileOutputStream fos = new FileOutputStream("notes", false);
				    		    ObjectOutputStream oos = new ObjectOutputStream(fos);
								note2.dateOfWriting=new Date();
								//note.heading.setText((note.heading.getText());
								
								note2.text.setText(noteText.getText());
								note2.heading.setText(noteHeading.getText());
								fileObjects.add(note2);//replace with updated note
								System.out.println("replaced");
								oos.writeObject(fileObjects);
								oos.close();
								fos.close();
    							System.out.println("file written");
    						
    						}
    						catch(Exception e)
    						{
    								//error.setText("Could not write reminder!");
    								e.printStackTrace();
    								try{
    									//ois.close();
    									
									//	fis.close();
										
    								}
    								catch(Exception ea)
    								{
    									ea.printStackTrace();
    								}
    								
    						}
    		
    	
    	
    	
    			
    			
    			editFlag=false;
    			addNote.setText("Save Note");
    			return;
    		}
    		try{// For storing this reminder in file we open the file, read the only ArrayList object it holds, update it with our reminder and then write it back in place of the original ArrayList object.
    					 		ArrayList fileObjects=null;
    							FileInputStream fis = new FileInputStream("notes");
				    		    ObjectInputStream ois = new ObjectInputStream(fis);
				    		    Object obj=null;
				    		    try{
				    		    
								if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						 }
				    		    }catch(EOFException ex)
    							{
    								
    							}
								if(fileObjects==null)
									fileObjects=new ArrayList();
								note.text.setText(noteText.getText());
								note.heading.setText(noteHeading.getText());
								
								fileObjects.add(note);
								ois.close();
								fis.close();
    							FileOutputStream fos = new FileOutputStream("notes", false);
				    		    ObjectOutputStream oos = new ObjectOutputStream(fos);
									
								oos.writeObject(fileObjects);
								oos.close();
								fos.close();
    							System.out.println("file written");
    						
    						}
    						catch(Exception e)
    						{
    								//error.setText("Could not write note!");
    								e.printStackTrace();
    								try{
    								//	ois.close();
    									
									//	fis.close();
										
    								}
    								catch(Exception ea)
    								{
    									ea.printStackTrace();
    								}
    								
    						}
    	}
    	if(ae.getSource().equals(refresh))
    	{
    		prepareReminderList();
    		repaint();
    		System.gc();
    		return;
    	}
    	if(ae.getSource().equals(delete))
    	{
    		
    							if(list==null || list.length==0)
    								return;//no reminders to delete
    							System.out.println("In delete");
    							ArrayList fileObjects=null;
    							try{
    								FileInputStream fis = new FileInputStream("reminders");
				    		    	ObjectInputStream ois = new ObjectInputStream(fis);
				    		    	Object obj=null;
				    		    	try{
				    		    
									if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						 }
				    		    }catch(EOFException ex)
    							{
    								
    							}
								if(fileObjects==null)
									fileObjects=new ArrayList();
								if(fileObjects.isEmpty())
									return;
								for(int i=0,j=0;i<list.length;i++,j++)
    							{
    								if(list[i].isSelected())
    								{	
    									fileObjects.remove(j);
    									j--;
    								}	
    							}
    		
								ois.close();
								fis.close();
    							FileOutputStream fos = new FileOutputStream("reminders", false);
				    		    ObjectOutputStream oos = new ObjectOutputStream(fos);
								
								oos.writeObject(fileObjects);
								oos.close();
								fos.close();
    							System.out.println("file written");
    						
    						}
    						catch(Exception e)
    						{
    								error.setText("Could not write reminder!");
    								e.printStackTrace();
    								try{
    									//ois.close();
    									
									//	fis.close();
										
    								}
    								catch(Exception ea)
    								{
    									ea.printStackTrace();
    								}
    								
    						}
    		
    		return;
    	}
    	//if the okay button is pressed
    	error.setText("");
    	ObjectInputStream ois=null;
    	ObjectOutputStream oos=null;
    	try{
    	if(Integer.parseInt(timeH.getText())>23 /*|| Integer.parseInt(repeatH.getText())>23*/)
    		error.setText("Error! Hours should be less than 24!");
    	else
    		if(Integer.parseInt(timeM.getText())>59 || Integer.parseInt(repeatM.getText())>59)
    			error.setText("Error! Minutes should be less than 60");
    		else
    			if(Integer.parseInt(timeS.getText())>59 || Integer.parseInt(repeatS.getText())>59)
    				error.setText("Error! Seconds should be less than 60");
    			else
    				if(Integer.parseInt(timeM.getText())>59)
    					error.setText("Error! Minutes should be less than 60");
    				else
    				{
    					Object repeatOption=selectRepeat.getSelectedItem();
    					if(repeatOption.equals("0 times"))
   					 		file.repeatTimes=0;
  					  	else
    						if(repeatOption.equals("indefinitely"))
    							file.repeatTimes=-1;
 					   		else
    							try{
    								file.repeatTimes=Integer.parseInt((String)repeatOption);
    								}catch(Exception e)
    								{
    									error.setText("Error! Give valid repetition count.");
    									newReminderPanel.add(error);
    									repaint();
    									return;
    								}    				
    				//	System.out.println(file.repeatTimes);
    					Date date=new Date();
 					   	date.setYear(currentCalendar.get(GregorianCalendar.YEAR)-1900);
    					date.setMonth(currentCalendar.get(GregorianCalendar.MONTH));
  					  	date.setDate(selectedDate);
				    	date.setHours(Integer.parseInt(timeH.getText()));	
    					date.setMinutes(Integer.parseInt(timeM.getText()));
  					  	date.setSeconds(Integer.parseInt(timeS.getText()));
				    	
				    	file.startDate=date;
    					file.repeatH=Integer.parseInt(repeatH.getText());
 					   	file.repeatM=Integer.parseInt(repeatM.getText());
				    	file.repeatS=Integer.parseInt(repeatS.getText());	
				    	file.msg=message.getText();
				    	if(file.startDate.before(new Date()))
				    	{
				    			System.out.println(file.startDate);
					    		error.setText("Cannot set reminder in the past!");
					    		newReminderPanel.add(error);
    							error.setHorizontalTextPosition(JLabel.LEFT);
    							repaint();
    							return;
				    	}	
				    	if(file.repeatTimes!=0 && file.repeatH==0 && file.repeatM==0 && file.repeatS==0)
    					{
    							error.setText("Give a period to repeat!");
    							newReminderPanel.add(error);
    							error.setHorizontalTextPosition(JLabel.LEFT);
    							repaint();
    							return;
    					}
    				
    				if(file.repeatTimes==0)
    				{	
    					 newTimer.schedule(new Task(file.msg,file.startDate),file.startDate);
    					// file.isScheduled=true;
    					 try{// For storing this reminder in file we open the file, read the only ArrayList object it holds, update it with our reminder and then write it back in place of the original ArrayList object.
    					 		ArrayList fileObjects=null;
    							FileInputStream fis = new FileInputStream("reminders");
				    		    ois = new ObjectInputStream(fis);
				    		    Object obj=null;
				    		    try{
				    		    
								if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						 }
				    		    }catch(EOFException ex)
    							{
    								
    							}
								if(fileObjects==null)
									fileObjects=new ArrayList();
								fileObjects.add(file);
								ois.close();
								fis.close();
    							FileOutputStream fos = new FileOutputStream("reminders", false);
				    		    oos = new ObjectOutputStream(fos);
								System.out.println(file.isScheduled);
									
								oos.writeObject(fileObjects);
								oos.close();
								fos.close();
    							System.out.println("file written");
    						
    						}
    						catch(Exception e)
    						{
    								error.setText("Could not write reminder!");
    								e.printStackTrace();
    								try{
    									ois.close();
    									
									//	fis.close();
										
    								}
    								catch(Exception ea)
    								{
    									ea.printStackTrace();
    								}
    								
    						}
    				}else
    					if(file.repeatTimes==-1)
    					{	
    						 newTimer.scheduleAtFixedRate(new Task(file.msg,file.startDate),file.startDate, 1000*(file.repeatH*3600+file.repeatM*60+file.repeatS));
    						// file.isScheduled=true;
    						 try{
    							ArrayList fileObjects=null;
    							FileInputStream fis = new FileInputStream("reminders");
				    		     ois = new ObjectInputStream(fis);
								Object obj=null;
								try{
									if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						 }
								}catch(EOFException ex)
								{
									
								}
								if(fileObjects==null)
									fileObjects=new ArrayList();
								fileObjects.add(file);
								ois.close();
								fis.close();
    							FileOutputStream fos = new FileOutputStream("reminders",false);
				    		    oos = new ObjectOutputStream(fos);
								oos.writeObject(fileObjects);
								oos.close();
								fos.close();
    							System.out.println("file written");
    						}catch(Exception e)
    						{
    								error.setText("Could not write reminder!");
    								e.printStackTrace();
    								ois.close();
    						}
    					}else
  						{
  							//repeat for a specified number of times
  							GregorianCalendar calen=new GregorianCalendar();
  							
  							calen.set(file.startDate.getYear()+1900, file.startDate.getMonth(), file.startDate.getDate(),file.startDate.getHours(),file.startDate.getMinutes(),file.startDate.getSeconds() );
							date=new Date();
  							for(int i=0; i<file.repeatTimes; i++)
  							{
  								/*	calen.getTime().setYear(calen.get(GregorianCalendar.YEAR)-1900);
    								date.setMonth(calen.get(GregorianCalendar.MONTH));
  								  	date.setDate(calen.get(GregorianCalendar.DATE));
							    	date.setHours(calen.get(GregorianCalendar.HOUR));	
    								date.setMinutes(calen.get(GregorianCalendar.MINUTE));
  								  	date.setSeconds(calen.get(GregorianCalendar.SECOND));
  									newTimer.schedule(new Task(file.msg),calen.getTime());
  								*/	
  									//calen.setTime(calen.getTime());
  									newTimer.schedule(new Task(file.msg, file.startDate),calen.getTime());
  								//	file.isScheduled=true;
  									System.out.println("reminding at "+date);
  									calen.add(GregorianCalendar.SECOND,file.repeatS);
  									calen.add(GregorianCalendar.MINUTE,file.repeatM);
  									calen.add(GregorianCalendar.HOUR,file.repeatH);
  									
  							}
							 try{
    							ArrayList fileObjects=null;
    							FileInputStream fis = new FileInputStream("reminders");
				    		    ois = new ObjectInputStream(fis);
								Object obj=null;
								try{
								
								if((obj = ois.readObject()) != null) {
                
               						 if (obj instanceof ArrayList) {
                
           						        // System.out.println(((ArrayList)obj).get(0));
         						           fileObjects=(ArrayList)obj;
          						      }
                
           						 }
								}catch(EOFException ex)
								{
								}
           						 if(fileObjects==null)
									fileObjects=new ArrayList();
								fileObjects.add(file);
								ois.close();
								fis.close();
    							FileOutputStream fos = new FileOutputStream("reminders",false);
				    		    oos = new ObjectOutputStream(fos);
								
								oos.writeObject(fileObjects);
								oos.close();
								fos.close();
    							System.out.println("file written");
    						}catch(Exception e)
    						{
    								error.setText("Could not write reminder!");
    								e.printStackTrace();
    								ois.close();
    						}  	
  						}
  						//System.out.print("Timer set at "+file.startDate);
  				}
    	}catch(Exception e)
    	{
    		error.setText("Field syntax error!");
    						
    	}	
  		
  		error.setAlignmentX(0.0f);
    	
  		newReminderPanel.add(error);
  			
  		repaint();
  		
    	//Make sure the input fields  have valid data. Store the data using serialized file
    	//Schedule reminder
    	
    }
    public void mouseClicked(MouseEvent me)
    {
    	//Component c=me.getComponent();
    	
    	if(me.getSource().equals(calendarEntry[0]))
    	{
    		//GregorianCalendar c=new GregorianCalendar();
	        
    		//GregorianCalendar c=new GregorianCalendar(year-1900, month+1,1);
	        reverseMonth();
	        return;
    	}
    	else
    	if(me.getSource().equals(calendarEntry[6]))
    	{
    		//GregorianCalendar c=new GregorianCalendar();
	        
    		//GregorianCalendar c=new GregorianCalendar(year-1900, month+1,1);
	        advanceMonth();
	        return;	//flip forward a month
    	
    	}else
    	{// Now set reminder
    		int i=0;
			for( i=14;i<=calendarEntry.length;i++)
    			if(calendarEntry[i]!=null && me.getSource().equals(calendarEntry[i]))
    			{
    			
    				break;
    			}
    		
			
    	
    		Date date=new Date(currentCalendar.get(GregorianCalendar.YEAR)-1900,currentCalendar.get(GregorianCalendar.MONTH),1);
        	int day=date.getDay();
        	//System.out.println("i="+i+", day="+day+",selectedDate="+selectedDate);
        	selectedDate=i-14-day+1;//save the date selected
        	//System.out.println(selectedDate);
    		makeNewReminderPanel();
    		//Task task=new Task();
    		
    		//newTimer.schedule(task,0, 3600*1000);	
    		//newTimer.schedule(task,0, 1000*10);
    	}
    	//for(int i=7;i<=calendarEntry.length;i++)
    	//	if(calendarEntry[i]!=null && me.getSource().equals(calendarEntry[i]))
    	//	{
    			//start registering reminder
    	//		break;
    	//	}
    	
    }
    public void mousePressed(MouseEvent me)
    {
    	
    }
    public void mouseEntered(MouseEvent me)
    {
    //	System.out.println("mouse entered "+ me.getComponent());
    }
    public void mouseExited(MouseEvent me)
    {
    }
    public void mouseReleased(MouseEvent me)
    {
    	
    }
    public void run() {
        pane.removeAll();
      /*  for (int i = 0; i < tabNumber; i++) {
            String title = "Tab " + i;
            pane.add(title, new JLabel(title));
            initTabComponent(i);
        }
       */
        pane.add("Add Reminder", mainPanel);
        initTabComponent(0);
        pane.add("Scheduled Reminders", reminderList);
        initTabComponent(1);
        pane.add("Notes", notesPanel);
        initTabComponent(2);
        pane.add("About", text);
        initTabComponent(3);
       // tabComponentsItem.setSelected(true);
        pane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
      //  scrollLayoutItem.setSelected(false);
        setSize(new Dimension(700, 400));
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    
    private void initTabComponent(int i) {
        //pane.setTabComponentAt(i, new ButtonTabComponent(pane));
    	pane.setTabComponentAt(i, null);
    
    }    
}
class CustomizedTimer 
{
	MyFrame mainScreen;
	public CustomizedTimer(String name)
	{
		//super(name);
		mainScreen=new MyFrame("Reminder");
	}
	public void testScreen()
	{
			mainScreen.run();
	}//call mainScreen.run() when the system tray icon is clicked 
 
	
}

public class Reminder {

    public Reminder() {
    }
    public static void main(String args[])
    {	
    	final CustomizedTimer timer=new CustomizedTimer("Reminder");
    //	Task task=new Task();
    	TrayIcon trayIcon = null;
     	if (SystemTray.isSupported()) {
         // get the SystemTray instance
         SystemTray tray = SystemTray.getSystemTray();
         // load an image
         final MenuItem defaultItem= new MenuItem("Main screen");
         final MenuItem item=new MenuItem("Exit");
         Image image = Toolkit.getDefaultToolkit().getImage("Calendaricon.png");
         // create a action listener to listen for default action executed on the tray icon
         ActionListener listener = new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 if(e.getSource().equals(defaultItem))
  	               timer.testScreen();
                 // execute default action of the application
                 if(e.getSource().equals(item))
                 	System.exit(0);
                 // ...
             }
         };
         // create a popup menu
         PopupMenu popup = new PopupMenu();
         // create menu item for the default action
         
         defaultItem.addActionListener(listener);
         popup.add(defaultItem);
         
         item.addActionListener(listener);
         popup.add(item);
         
         /// ... add other items
         // construct a TrayIcon
         //trayIcon = new TrayIcon(image, "Reminder", popup);
         // set the TrayIcon properties
      
      	  trayIcon = new TrayIcon(image, "Reminder", popup);
         
      	  trayIcon.addActionListener(listener);
         // ...
         // add the tray image
         try {
             tray.add(trayIcon);
         } catch (AWTException e) {
             e.printStackTrace();
         }
         // ...
     } else {
         // disable tray option in your application or
         // perform other actions
         
     }
    
  //  	timer.schedule(task,0,3600*1000);// set to 1 hour reminders
    	
    }
    
}
