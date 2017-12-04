package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Controller.GameController;

@SuppressWarnings("serial")
public class FileScreen extends JFrame {
    private JFileChooser fileChooser;
    private GameController controller = GameController.getInstance();
    
	public FileScreen() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(44, 128, 65));
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(new File("."));
		panel.add(fileChooser);
		
		setSize(600, 470);
		add(panel);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);
	}
	
	public void showSaveScreen() {
		fileChooser.setDialogTitle("Salvar Jogo");
		
		int returnVal = fileChooser.showSaveDialog(FileScreen.this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Saving " + file.getAbsolutePath());
            controller.save(file.getAbsolutePath());
        }
	}
	
	public void showChooseFileScreen() {
		fileChooser.setDialogTitle("Recuperar Jogo Salvo");
		
		int returnVal = fileChooser.showSaveDialog(FileScreen.this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
        }
	}
	
}
