package com.concordia.riskgame.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.concordia.riskgame.model.Modules.Continent;
import com.concordia.riskgame.model.Modules.Country;
import com.concordia.riskgame.model.Modules.Gameplay;
import com.concordia.riskgame.model.Modules.Map;
import com.concordia.riskgame.model.Modules.Player;
import com.concordia.riskgame.utilities.MapTools;
import com.concordia.riskgame.view.MapEditorView;
import com.concordia.riskgame.view.StartUpPhaseView;
/**
 * This class initializes the StartUpPhase View and invoke corresponding gameplay functions as per user-view interaction.
 */
public class StartupPhaseController implements ActionListener  {
	
	private List<String> countries;
	private Gameplay gameplay;
	private StartUpPhaseView sView;
	private MapTools mapTool=new MapTools();
	private MapEditorController mapEditor=new MapEditorController(new MapEditorView(new Map()));
		
	/**
	 * Instantiates a new startup phase controller.
	 *
	 * @param startupView : The startup view object
	 */
	public StartupPhaseController(StartUpPhaseView startupView) {
		this.countries=new ArrayList<String>();
		this.gameplay = Gameplay.getInstance();
		this.sView=startupView;
		initView();
	}


	/**
	 * Getter method to get counties list
	 * @return counties
	 */
	public List<String> getCountries() {
		return countries;
	}


	/**
	 * Setter method to set countries.
	 * @param countries Countries
	 */
	public void setCountries(ArrayList<String> countries) {
		this.countries = countries;
	}	

	/**
	 * Initializes the Startup phase view.
	 */
	public void initView() {
		sView.initaliseUI();
		initController();
		
	}
	
	
	/**
	 * Initialise the controller.
	 */
	public void initController() {
		sView.getAddPlayerButton().addActionListener(this);
		sView.getRemovePlayerButton().addActionListener(this);
		sView.getPopulateCountriesButton().addActionListener(this);
		sView.getMapSelectorButton().addActionListener(this);
		sView.getShowMapButton().addActionListener(this);
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource()==sView.getMapSelectorButton())
		{
			
			Map existingMap=new Map();
			String sFinal=mapTool.pickMapFile(existingMap);
			System.out.println(sFinal);
			if(sFinal == null || (sFinal.isEmpty())) {
				
				System.out.println("Map Path Empty");
			}
			else {
				boolean isMapValid=mapTool.parseAndValidateMap(existingMap,3);
				if(isMapValid) {
					JOptionPane.showMessageDialog(null, "Map successfully loaded");
					gameplay.setSelectedMap(existingMap);
					System.out.println(gameplay.getSelectedMap().getAuthorName());
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid Map selected");
					System.out.println(existingMap.getErrorMessage());
				}
			}
				}

		else if(event.getSource()==sView.getAddPlayerButton())
		{	if(gameplay.getPlayerCount()==0)
				gameplay.setPlayerCount(Integer.parseInt(sView.getPlayerCount().getSelectedItem().toString()));

			if(sView.getPlayerName().getText().contentEquals(""))
				JOptionPane.showMessageDialog(null,
						"Please enter a player name", "Error Message",
						JOptionPane.ERROR_MESSAGE);
			if(gameplay.getPlayers().size()>=gameplay.getPlayerCount()) {
				
				JOptionPane.showMessageDialog(null,
						"Player limit reached.Cannot add anymore players", "Error Message",
						JOptionPane.ERROR_MESSAGE);
			}
			
			else
			{
				String message=gameplay.addPlayer(sView.getPlayerName().getText());
				JOptionPane.showMessageDialog(null,
						message, "Message",
						JOptionPane.INFORMATION_MESSAGE);
				sView.getModel().clear();
				for(int i=0;i<gameplay.getPlayers().size();i++)
					sView.getModel().add(i, gameplay.getPlayers().get(i).getPlayerName());
				
			}



		}

		else if(event.getSource()==sView.getRemovePlayerButton())
		{
			if(!(sView.getRemovePlayerName().getText().contentEquals(""))) {
				String message=gameplay.removePlayer(sView.getRemovePlayerName().getText());
				JOptionPane.showMessageDialog(null,
						message, "Message",
						JOptionPane.INFORMATION_MESSAGE);
								
				sView.getModel().clear();
				for(int i=0;i<gameplay.getPlayers().size();i++)
					sView.getModel().add(i, gameplay.getPlayers().get(i).getPlayerName());
				
			}
			else {
				JOptionPane.showMessageDialog(null,
						"Please enter a player name", "Error Message",
						JOptionPane.ERROR_MESSAGE);
					
			}

		}



		else if(event.getSource()==sView.getPopulateCountriesButton())
		{
			String message=gameplay.validateStartupInputs();
			if(!message.contentEquals("Success"))
			{

				JOptionPane.showMessageDialog(null,
						message, "Error Message",
						JOptionPane.ERROR_MESSAGE);

			}
			else
			{
				gameplay.initialisePlayers();
				JOptionPane.showMessageDialog(null,
						"Player Initialisation complete.Please use command line interface for the rest of gameplay", "Information Message",
						JOptionPane.INFORMATION_MESSAGE);
					}

		}
		
		
		else if (event.getSource()==sView.getShowMapButton())
		{
			if(gameplay.getSelectedMap()==null)
				 JOptionPane.showMessageDialog(null, "No Map Selected!");
			else
				mapEditor.showMapService(gameplay.getSelectedMap());
		}

	}
	



	
		
}
