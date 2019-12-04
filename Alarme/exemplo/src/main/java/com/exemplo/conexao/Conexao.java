package com.exemplo.conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class Conexao implements SerialPortEventListener {
	
	private static final int NUL = (byte)'\0';
	private static final int LF  = (byte)'\n';
	private static final int CR  = (byte)'\r';
	
	private List<String> portas;
	
	private SerialPort serialPort;
	private CommPortIdentifier portIdentifier;
	private CommPort commPort;
	
    //private static List<String> resultList = Collections.synchronizedList(new ArrayList<>());
    //private Thread readThread = null;
    //private Thread writeThread = null;
    
	private BufferedReader leitura = null;
    private OutputStream escrita = null;

	private int baudRate;
	private int dataBits;
	private int parity;
	private int stopbits;
   
	private boolean portaAberta = false;	
    private int totalPortas = 0; 
	
	
	public Conexao() {
		this.baudRate = 9600;
		this.dataBits = SerialPort.DATABITS_8;
		this.parity = SerialPort.PARITY_NONE;
		this.stopbits = SerialPort.STOPBITS_1;
		this.totalPortas++;
	}

	
	public Conexao(int baudRate) {
		this.baudRate = baudRate;
		this.dataBits = SerialPort.DATABITS_8;
		this.parity = SerialPort.PARITY_NONE;
		this.stopbits = SerialPort.STOPBITS_1;
		totalPortas++;
	}
		
	public Conexao(int baudRate, int dataBits, int parity, int stopbits) {
		//this.portas = portas;
		this.baudRate = baudRate;
		this.dataBits = dataBits;
		this.parity = parity;
		this.stopbits = stopbits;
		totalPortas++;
	}


	public List<String> leiaPortas() {
		System.out.println("lendo as portas");
		portas = new ArrayList<String>();
		Enumeration<?> portasSistema = CommPortIdentifier.getPortIdentifiers();
		while( portasSistema.hasMoreElements() ) {
			portIdentifier = (CommPortIdentifier) portasSistema.nextElement();
			if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				portas.add(portIdentifier.getName());
			}
		}
		System.out.println(" tamanho "+ portas.size());
		return portas;
	}
	
	
	public boolean openConnetion(String porta) {
		if (!exists(porta)) {
			return false;
		}
		
		if ( portaAberta == true ) {
			 portaAberta = false;
			 close();
		}
		
		if (portaAberta == false) {
			
			try {
				portIdentifier = CommPortIdentifier.getPortIdentifier(porta);
				if (portIdentifier.isCurrentlyOwned()) {
					portaAberta = true;
					return portaAberta;
				}
				if ( portaAberta == false ) {
					commPort = portIdentifier.open("",2000);
					serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(getBaudRate(), 
							                       getDataBits(), 
							                       getStopbits(), 
							                       getParity());
					serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
					leitura = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
					escrita = serialPort.getOutputStream();
					serialPort.addEventListener(this);
					serialPort.notifyOnDataAvailable(true);
					portaAberta = true;
					
					
				}
			}catch (IOException e) {
				e.printStackTrace();
			}catch (PortInUseException e) {
				e.printStackTrace();
			}catch(NoSuchPortException e) {
				e.printStackTrace();
			}catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
			}catch (TooManyListenersException e) {
				e.printStackTrace();
			}
			
		}
		return portaAberta;
	}
	
	
		
	public synchronized void close() {
		
		if ( serialPort != null ) {
			 serialPort.removeEventListener();
			 serialPort.close();
		}
		
	}


	private boolean exists(String porta) {
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(porta);
		} catch(NoSuchPortException e) {
			return false;
		}
		return true;
	}
	
	
	@Override
	public void serialEvent(SerialPortEvent event) {
	
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int result = leitura.read();
				System.out.println("recebendo dados na serial "+result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	
	public void sendData(String data) {

		try {
			if (this.portaAberta) {
				System.out.println("enviando dados na serial "+ data);
				escrita.write(data.getBytes());
				escrita.flush();
			} 
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public List<String> getPortas() {
		return portas;
	}

	public void setPortas(List<String> portas) {
		this.portas = portas;
	}


	public int getBaudRate() {
		return baudRate;
	}


	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}


	public int getDataBits() {
		return dataBits;
	}


	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}


	public int getParity() {
		return parity;
	}


	public void setParity(int parity) {
		this.parity = parity;
	}


	public int getStopbits() {
		return stopbits;
	}


	public void setStopbits(int stopbits) {
		this.stopbits = stopbits;
	}

	
	public SerialPort getSerialPort() {
		return serialPort;
	}


	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}


	public CommPortIdentifier getPortIdentifier() {
		return portIdentifier;
	}


	public void setPortIdentifier(CommPortIdentifier portIdentifier) {
		this.portIdentifier = portIdentifier;
	}


	public CommPort getCommPort() {
		return commPort;
	}


	public void setCommPort(CommPort commPort) {
		this.commPort = commPort;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
