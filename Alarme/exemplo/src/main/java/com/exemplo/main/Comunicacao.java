package com.exemplo.main;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.exemplo.conexao.Conexao;
import com.exemplo.model.ComunicacaoMaquina;
import com.exemplo.reports.GeraRelatorio;
import com.exemplo.service.MaquinaService;

import net.sf.jasperreports.engine.JRParameter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.util.Date;
import java.util.HashMap;
import java.util.Calendar;
import javax.swing.JLayeredPane;
import java.awt.Color;



public class Comunicacao extends JFrame {

	private static final long serialVersionUID = 5982264099348912699L;

	private JPanel contentPane;
	
	private JComboBox<String> portaComboBox;
	private JComboBox<String> baudRateComboBox;
	private JComboBox<String> dataBitsComboBox;
	private JComboBox<String> paridadeComboBox;
	private JComboBox<String> stopBitsComboBox;

	private JButton btnConectar;
	private JButton btnDesconectar;
	
	private String baudRate[] = {"9600"};
	private String dataBits[] = {"8"};
	private String paridade[] = {"0"};
	private String stopBits[] = {"1"};
	
	
	private boolean portOpen = false;
	private int     intBaudRate = 0;
	private int     intDataBits = 0;
	private int     intParidade = 0; 
	private int     intStopBits = 0;
	private int 	estados = 0;
	private int 	estados2 = 0;
	private long total = 0 ;
	private long  finish = 0;
	private long  start = 0;
	private Conexao conexao;
	
	private String  dir;
	private JButton btnLiga;
	private JButton btnDesliga;
	private JButton btn_sensor1_ON;
	private JButton btnOff;
	private JButton btn_sensor2_ON;
	private JButton btnOff_1;
	
	private JTextField txtEstado;
	private JTextField txtData;
	private JButton btn_Relatorio;
	private JLayeredPane layeredPane_1;
	private JPanel panel_sensor1;
	private JPanel panel_sensor2;
	private JPanel panel_sirene;
	private JTextField textEstado2;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Comunicacao frame = new Comunicacao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Comunicacao() {
		
		getPathLib();
		
		initComponents();
		
		leiaBaudRate();
		leiaDataBits();
		leiaParidade();
		leiaStopBits();
		
		leiaPortas();
		createEvents();
	}
	
	
	
	private void getPathLib() {
		setDir(System.getProperty("user.dir"));
		try {
			System.load(getDir()+"\\rxtxSerial.dll");
			System.load(getDir()+"\\rxtxParallel.dll");
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void createEvents() {
		
		btn_Relatorio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				MaquinaService maquinaService = new MaquinaService();
				List<ComunicacaoMaquina> listaMaquina = maquinaService.listarTodosMaquinas();
				String nomeArquivo = "relatorio_maquina";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(JRParameter.REPORT_LOCALE, new Locale("pt","BR"));
				GeraRelatorio geraRelatorio = new GeraRelatorio(nomeArquivo, params, listaMaquina);
				geraRelatorio.callReport();
				
				
			}
		});
	}
	
	private void leiaPortas() {
		Conexao conexao = new Conexao();
		
		List<String> portasSistema = new ArrayList<>();
		
		portasSistema = conexao.leiaPortas();
		
		if (portasSistema.isEmpty() ) {
			JOptionPane.showMessageDialog(null,"Nenhuma porta encontrada! - Verifique", 
					                      "Erro", JOptionPane.ERROR_MESSAGE);
		}
		
	
		portaComboBox.setModel(new DefaultComboBoxModel<String>(
				                     portasSistema.toArray(new String[portasSistema.size()]))
				               );
	}
	
	private void leiaBaudRate() {
		baudRateComboBox.setModel(new DefaultComboBoxModel<String>(this.getBaudRate()));
	}
	
	
	private void leiaDataBits() {
		dataBitsComboBox.setModel(new DefaultComboBoxModel<String>(this.dataBits));
	}
	
	
	private void leiaParidade() {
		paridadeComboBox.setModel(new DefaultComboBoxModel<String>(this.getParidade()));
	}
	
	
	private void leiaStopBits() {
		stopBitsComboBox.setModel(new DefaultComboBoxModel<String>(this.getStopBits()));
	}
	
	
	private void checaBotao() {
		if(estados == 0) {
			

			btnLiga.setEnabled(true);

			btn_sensor1_ON.setEnabled(false);
			btn_sensor2_ON.setEnabled(false);
			btnOff.setEnabled(false);
			btnOff_1.setEnabled(false);
			btnDesliga.setEnabled(false);
		
			
		}else {
			
			btnLiga.setEnabled(false);
			btnDesliga.setEnabled(true);
			
			btn_sensor1_ON.setEnabled(true);
			btn_sensor2_ON.setEnabled(true);
			btnOff.setEnabled(true);
			btnOff_1.setEnabled(true);

		}
		 
	}
	
	
	private void criarConexao(ActionEvent e) {
		
		if (Objects.isNull(conexao)) {
			if (getIntBaudRate()==0) {
				conexao = new Conexao();
			} else {
				conexao = new Conexao(getIntBaudRate());
			}
			portOpen = conexao.openConnetion(getPortaComboBox().getSelectedItem().toString());
		}
  	
	}
	
	protected ComunicacaoMaquina pegarDadosMaquinaFromTela() {
		ComunicacaoMaquina maquina = new ComunicacaoMaquina();
		Date date=new Date();
		
		maquina.setEstado(txtEstado.getText());
		maquina.setSirene(textEstado2.getText());
		maquina.setData(date);
        return maquina;		
	}
	
	private void enviarMensagemDesliga(ActionEvent e) {
		Thread tarefa = new Thread() {
			String c="D";
			public void run() {
			          conexao.sendData(c);
			          
			          estados = 0;
			          estados2=2;
			          atualizaLabel();
			          checaBotao(); 
			          try {
			        	  Thread.sleep(10000);
			          }catch (InterruptedException e) {
					}
			}
		};
		tarefa.start();
	}
	
	private void enviarMensagemLiga(ActionEvent e) {
        finish=0;
        start =0;
        total=0;
        estados = 1;
        estados2 = 2;
		Thread tarefa = new Thread() {
			String c="L";
			public void run() {
			          conexao.sendData(c);
			          atualizaLabel();
			          checaBotao(); 
			          try {
			        	  Thread.sleep(10000);
			          }catch (InterruptedException e) {
					}
			}
		};
		tarefa.start();
	}
	
	private void atualizaLabel() {
		
		MaquinaService maquinaService = new MaquinaService();
		ComunicacaoMaquina maquina = new ComunicacaoMaquina();
		Date date = new Date();
		 
		txtData.setText("Data: "+date);	
		
        total = finish - start;
        if(total>3 ) {
        estados2= 1;}
		if(estados == 0) {
			
			txtEstado.setText("Desligado");
		
		}
		if(estados == 1) {
			txtEstado.setText("Ligado");
			
		}
		if(estados == 2) {
			txtEstado.setText("Detectado");

		}

		if(estados2 == 1) {
			textEstado2.setText("Ativado");
			panel_sirene.setBackground(Color.GREEN);
			conexao.sendData("S");
		}
		if(estados2 == 2) {
			textEstado2.setText("Desativado");

			
		}
		maquina = pegarDadosMaquinaFromTela();
		maquinaService.salvarMaquina(maquina);
	}
	
	
	private void initComponents() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 586, 389);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
		);
		
		JLayeredPane layeredPane = new JLayeredPane();
		tabbedPane.addTab("Conexão", null, layeredPane, null);
		
		JLabel lblPorta = new JLabel("Porta:");
		lblPorta.setBounds(34, 34, 50, 14);
		layeredPane.add(lblPorta);
		
		portaComboBox = new JComboBox<String>();
		portaComboBox.setBounds(94, 34, 429, 20);
		layeredPane.add(portaComboBox);
		
		JLabel lblBaudRate = new JLabel("Baud Rate:");
		lblBaudRate.setBounds(10, 65, 74, 14);
		layeredPane.add(lblBaudRate);
		
		baudRateComboBox = new JComboBox<String>();
		baudRateComboBox.setBounds(94, 65, 429, 20);
		layeredPane.add(baudRateComboBox);
		
		JLabel lblDataBits = new JLabel("Data Bits:");
		lblDataBits.setBounds(17, 103, 67, 14);
		layeredPane.add(lblDataBits);
		
		dataBitsComboBox = new JComboBox<String>();
		dataBitsComboBox.setBounds(94, 103, 429, 20);
		layeredPane.add(dataBitsComboBox);
		
		JLabel lblParidade = new JLabel("Paridade:");
		lblParidade.setBounds(17, 134, 67, 14);
		layeredPane.add(lblParidade);
		
		paridadeComboBox = new JComboBox<String>();
		paridadeComboBox.setBounds(94, 134, 429, 20);
		layeredPane.add(paridadeComboBox);
		
		JLabel lblStopBits = new JLabel("Stop Bits:");
		lblStopBits.setBounds(17, 172, 67, 14);
		layeredPane.add(lblStopBits);
		
		stopBitsComboBox = new JComboBox<String>();
		stopBitsComboBox.setBounds(94, 172, 429, 20);
		layeredPane.add(stopBitsComboBox);
		
		btnConectar = new JButton("Conectar");
		btnConectar.setBounds(142, 249, 110, 23);
		layeredPane.add(btnConectar);
		
		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setBounds(355, 249, 110, 23);
		layeredPane.add(btnDesconectar);
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				criarConexao(e);
			}
		});
		
		layeredPane_1 = new JLayeredPane();
		tabbedPane.addTab("Alarme Residencial", null, layeredPane_1, null);
		
		txtEstado = new JTextField();
		txtEstado.setEditable(false);
		txtEstado.setBounds(37, 121, 86, 20);
		layeredPane_1.add(txtEstado);
		txtEstado.setColumns(10);
		
		btnLiga = new JButton("Liga");
		btnLiga.setBounds(37, 152, 85, 23);
		layeredPane_1.add(btnLiga);
		
		btnDesliga = new JButton("Desliga");
		btnDesliga.setBounds(38, 186, 85, 23);
		layeredPane_1.add(btnDesliga);
		 
		 txtData = new JTextField();
		 txtData.setEditable(false);
		 txtData.setBounds(152, 53, 268, 20);
		 layeredPane_1.add(txtData);
		 txtData.setColumns(10);
		 
		 btn_Relatorio = new JButton("Relatorio");
		 btn_Relatorio.setBounds(243, 261, 120, 23);
		 layeredPane_1.add(btn_Relatorio);
		 
		 JLabel lblDataEHora = new JLabel("Data e Hora:");
		 lblDataEHora.setBounds(243, 28, 75, 14);
		 layeredPane_1.add(lblDataEHora);
		 
		 JLabel lblEstadoAtual = new JLabel("Alarme Estado:");
		 lblEstadoAtual.setBounds(37, 96, 75, 14);
		 layeredPane_1.add(lblEstadoAtual);
		 
		 panel_sensor1 = new JPanel();
		 panel_sensor1.setBackground(Color.RED);
		 panel_sensor1.setBounds(331, 124, 39, 36);
		 layeredPane_1.add(panel_sensor1);
		 
		 JPanel panel_sirene = new JPanel();
		 panel_sirene.setBackground(Color.RED);
		 panel_sirene.setBounds(200, 124, 39, 36);
		 layeredPane_1.add(panel_sirene);
		 
		 panel_sensor2 = new JPanel();
		 panel_sensor2.setBackground(Color.RED);
		 panel_sensor2.setBounds(460, 124, 39, 36);
		 layeredPane_1.add(panel_sensor2);
		 
		 JLabel lblNewLabel = new JLabel("Sensor1");
		 lblNewLabel.setBounds(331, 99, 46, 14);
		 layeredPane_1.add(lblNewLabel);
		 
		 JLabel lblNewLabel_1 = new JLabel("Sensor2");
		 lblNewLabel_1.setBounds(460, 99, 46, 14);
		 layeredPane_1.add(lblNewLabel_1);
		 
		 JButton btn_sensor1_ON = new JButton("ON");
		 
		 btn_sensor1_ON.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {
		          estados = 2;
			         finish=0;
			         start =0;
			         total=0;
		          start = (System.currentTimeMillis()/1000);

		 		 panel_sensor1.setBackground(Color.GREEN);
				Thread tarefa = new Thread() {
					String c="A";
					public void run() {
						

						while(true) {
					          conexao.sendData(c);
					          atualizaLabel();
					          checaBotao(); 
					          
					          try {
					        	  Thread.sleep(10000);
					          }catch (InterruptedException e) {
							}
					}     
					}
				};
				tarefa.start();

		 	}
		 });
		 
		 
		 btn_sensor1_ON.setBounds(320, 177, 75, 23);
		 layeredPane_1.add(btn_sensor1_ON);
		 
		 JButton btn_sensor2_ON = new JButton("ON");
		 btn_sensor2_ON.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		          estados = 2;
			         finish=0;
			         start =0;
			         total=0;
		          start = (System.currentTimeMillis()/1000);
		          
		 		 panel_sensor2.setBackground(Color.GREEN);
					
					Thread tarefa = new Thread() {
						String c="B";
						public void run() {
							
							while(true) {
						          conexao.sendData(c);

						          atualizaLabel();
						          checaBotao(); 
						          try {
						        	  Thread.sleep(10000);
						          }catch (InterruptedException e) {
								}
							}
						}
					};
					tarefa.start();
		 		
		 	}
		 });
		 btn_sensor2_ON.setBounds(444, 177, 75, 23);
		 layeredPane_1.add(btn_sensor2_ON);
		 
		 JButton btnOff = new JButton("OFF");
		 btnOff.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {

		 		finish = (System.currentTimeMillis()/1000);
		 		estados=1;

		 		panel_sensor1.setBackground(Color.RED);
		         atualizaLabel();
		         checaBotao();

		 		
		 	}
		 });
		 btnOff.setBounds(320, 199, 75, 23);
		 layeredPane_1.add(btnOff);
		 
		 JButton btnOff_1 = new JButton("OFF");
		 btnOff_1.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		
		 		panel_sensor2.setBackground(Color.RED);
		 		finish = (System.currentTimeMillis()/1000);
		 		estados=1;
	
		         atualizaLabel();
		         checaBotao();

		 	}
		 });
		 btnOff_1.setBounds(444, 199, 75, 23);
		 layeredPane_1.add(btnOff_1);
		 
		 JLabel lblSirene = new JLabel("Sirene");
		 lblSirene.setBounds(200, 96, 46, 14);
		 layeredPane_1.add(lblSirene);
		 
		 textEstado2 = new JTextField();
		 textEstado2.setEditable(false);
		 textEstado2.setColumns(10);
		 textEstado2.setBounds(182, 187, 86, 20);
		 layeredPane_1.add(textEstado2);
		 

		 

		btnDesliga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarMensagemDesliga(e);
			}
		});
		btnLiga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				enviarMensagemLiga(e);
				
			}

			
		});
		contentPane.setLayout(gl_contentPane);
		
		leiaPortas();
		atualizaLabel();
		//Date data = new Date();
		//txtData.setText("Data: "+data);
		
	}

	public JComboBox<String> getPortaComboBox() {
		return portaComboBox;
	}

	public void setPortaComboBox(JComboBox<String> portaComboBox) {
		this.portaComboBox = portaComboBox;
	}

	public JComboBox<String> getBaudRateComboBox() {
		return baudRateComboBox;
	}

	public void setBaudRateComboBox(JComboBox<String> baudRateComboBox) {
		this.baudRateComboBox = baudRateComboBox;
	}

	public JComboBox<String> getDataBitsComboBox() {
		return dataBitsComboBox;
	}

	public void setDataBitsComboBox(JComboBox<String> dataBitsComboBox) {
		this.dataBitsComboBox = dataBitsComboBox;
	}

	public JComboBox<String> getParidadeComboBox() {
		return paridadeComboBox;
	}

	public void setParidadeComboBox(JComboBox<String> paridadeComboBox) {
		this.paridadeComboBox = paridadeComboBox;
	}

	public JComboBox<String> getStopBitsComboBox() {
		return stopBitsComboBox;
	}

	public void setStopBitsComboBox(JComboBox<String> stopBitsComboBox) {
		this.stopBitsComboBox = stopBitsComboBox;
	}

	public String[] getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(String[] baudRate) {
		this.baudRate = baudRate;
	}

	public String[] getDataBits() {
		return dataBits;
	}

	public void setDataBits(String[] dataBits) {
		this.dataBits = dataBits;
	}

	public String[] getParidade() {
		return paridade;
	}

	public void setParidade(String[] paridade) {
		this.paridade = paridade;
	}

	public String[] getStopBits() {
		return stopBits;
	}

	public void setStopBits(String[] stopBits) {
		this.stopBits = stopBits;
	}

	public boolean isPortOpen() {
		return portOpen;
	}

	public void setPortOpen(boolean portOpen) {
		this.portOpen = portOpen;
	}

	public int getIntBaudRate() {
		return intBaudRate;
	}

	public void setIntBaudRate(int intBaudRate) {
		this.intBaudRate = intBaudRate;
	}

	public int getIntDataBits() {
		return intDataBits;
	}

	public void setIntDataBits(int intDataBits) {
		this.intDataBits = intDataBits;
	}

	public int getIntParidade() {
		return intParidade;
	}

	public void setIntParidade(int intParidade) {
		this.intParidade = intParidade;
	}

	public int getIntStopBits() {
		return intStopBits;
	}

	public void setIntStopBits(int intStopBits) {
		this.intStopBits = intStopBits;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
}