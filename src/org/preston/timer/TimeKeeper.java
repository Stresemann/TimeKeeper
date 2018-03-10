package org.preston.timer;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.awt.event.ActionEvent;

public class TimeKeeper extends JFrame {

	private static final String FILE_LOCATION = "times.csv";
	private JPanel contentPane;
	private static Date startDate;
	private static Date endDate;
	private static FileWriter pw;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TimeKeeper frame = new TimeKeeper();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TimeKeeper() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JButton btnStartTimer = new JButton("Start Timer");
		btnStartTimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("button pressed " + new Date());
				try {
					if(startDate == null) {
						writeStartDateToFile();
			            btnStartTimer.setText("Timer Started");
					} else {
						writeEndDateToFile();
			            startDate = null;
			            btnStartTimer.setText("Start Timer");
					}
			        
			            pw.flush();
			            pw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}

			
		});
		contentPane.add(btnStartTimer, BorderLayout.CENTER);
	}

	private void writeStartDateToFile() throws IOException {
		File f = new File(FILE_LOCATION);
		if(!f.exists()) {
			writeHeaderToFile();
		} else if(!fileEndsInReturn()){
			pw.append("\n");
		}
		pw = new FileWriter(FILE_LOCATION,true);
		startDate = new Date();
		pw.append(startDate.toString());
		pw.append(",");
		endDate = null;
	}
	
	private void writeEndDateToFile() throws IOException {
		pw = new FileWriter(FILE_LOCATION,true);
		endDate = new Date();
		pw.append(endDate.toString());
		pw.append(",");
		pw.append(showDescriptionDialog(new String()));
		pw.append(",");
		pw.append(calculateDifference(startDate, endDate));
		pw.append("\n");
	}

	private String showDescriptionDialog(String s) {
		try {
			s = (String)JOptionPane.showInputDialog(
                    null,
                    "In the box below describe what you did:",
                    "What did you do?",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "I fixed...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Add double quotes to beginning and end in case the user includes a comma
		if(s != null)
			return '"' + s + '"';
		else 
			return "";
	}
	
	/**
	 * Test if the file ends in a return character 
	 * will prevent lines becoming out of sync if a user 
	 * shuts down the program before ending the timer
	 * @return
	 */
	private boolean fileEndsInReturn() {
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_LOCATION))) {
			Integer character = null;
			Integer lastCharacter = 1;
			while ((character = br.read()) != -1) {
				lastCharacter = character;
			}
			return lastCharacter == 10;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void writeHeaderToFile() throws IOException {
		pw = new FileWriter(FILE_LOCATION,true);
		pw.append("Start Date");
		pw.append(",");
		pw.append("End Date");
		pw.append(",");
		pw.append("Description");
		pw.append(",");
		pw.append("Time Elapsed");
		pw.append("\n");
        pw.flush();
        pw.close();
	}

	@Override
	public void dispose() {
	    if(startDate != null) {
            try {
            	pw = new FileWriter(FILE_LOCATION,true);
		    	endDate = new Date();
				pw.append(endDate.toString());
	            pw.append(",");
	            pw.append(",");
				pw.append(calculateDifference(startDate, endDate));
	            pw.append("\n");
	            pw.flush();
	            pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    super.dispose();
	}
	
	public static String calculateDifference(Date startDate, Date endDate){

		//milliseconds
		long different = endDate.getTime() - startDate.getTime();

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		return elapsedHours +":" + elapsedMinutes ;

	}
	
	
}
