package com.apap.tutorial7.controller;

import com.apap.tutorial7.model.FlightModel;
import com.apap.tutorial7.model.PilotModel;
import com.apap.tutorial7.service.FlightService;
import com.apap.tutorial7.service.PilotService;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * FlightController
 */
@Controller
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private PilotService pilotService;
	
//	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
//	private String add(@PathVariable(value = "licenseNumber") String licenseNumber, Model model) {
//		FlightModel flight = new FlightModel();
//		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
//		flight.setPilot(pilot);
//		
//		model.addAttribute("flight", flight);
//		model.addAttribute("title", "Add Flight");
//		return "addFlight";
//	}
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
	private String add(@PathVariable(value = "licenseNumber") String licenseNumber, Model model) {
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
		List<FlightModel> flightList = new ArrayList<FlightModel>();
		
		FlightModel flight = new FlightModel();
		flight.setPilot(pilot);
		flightList.add(flight);
		
		pilot.setPilotFlight(flightList);
	
		model.addAttribute("pilot", pilot);
		model.addAttribute("title", "Add Flight");
		return "addFlight";
	}
	
	@RequestMapping(value="/flight/add/{licenseNumber}", params={"addRow"}, method = RequestMethod.POST)
	public String addRow(@PathVariable(value = "licenseNumber") String licenseNumber,
			@ModelAttribute PilotModel pilot, Model model) {
	    
		pilot.getPilotFlight().add(new FlightModel());
	    model.addAttribute("pilot", pilot);
	    return "addFlight";
	}

	@RequestMapping(value="/flight/add/{licenseNumber}", params={"removeRow"}, method=RequestMethod.POST)
	public String removeRow(@PathVariable(value = "licenseNumber") String licenseNumber,
			@ModelAttribute PilotModel pilot, final BindingResult bindingResult, 
	        final HttpServletRequest req, Model model) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));

	    pilot.getPilotFlight().remove(rowId.intValue());
	    model.addAttribute("pilot", pilot);
	    return "addFlight";
	}
	
//	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
//	private String add(@PathVariable(value = "licenseNumber") String licenseNumber, Model model) {
//		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
//		
//		model.addAttribute("pilot", pilot);
//		model.addAttribute("title", "Add Flight");
//		return "addFlight";
//	}
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", params= {"submit"}, method = RequestMethod.POST)
	private String addFligthSubmit(@ModelAttribute PilotModel pilot, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(pilot.getLicenseNumber()).get();
		
		for(FlightModel flight : pilot.getPilotFlight()) {
			flight.setPilot(archive);
			flightService.addFlight(flight);
		}
		
		model.addAttribute("title", "Add Flight");
		return "add";
	}
	
//	@RequestMapping(value = "/flight/delete/{id}")
//	private String deleteFlight(@PathVariable(value = "id") long id, Model model) {
//		FlightModel archive = flightService.getFlightDetailById(id);
//		
//		flightService.deleteFlight(archive);
//		return "delete";
//	}
	
	@RequestMapping(value="/flight/delete", method= RequestMethod.POST)
	private String deleteFlight(@ModelAttribute PilotModel pilot, Model model) {
		for(FlightModel flight : pilot.getPilotFlight()) {
			flightService.deleteFlight(flightService.getFlightDetailById(flight.getId()));
		}
		model.addAttribute("title", "Delete FLight");
		return "delete";
	}
	
	@RequestMapping(value = "/flight/update/{id}", method = RequestMethod.GET)
	private String update(@PathVariable(value = "id") long id, Model model) {
		FlightModel archive = flightService.getFlightDetailById(id);
		
		model.addAttribute("flight", archive);
		model.addAttribute("title", "Update Flight");
		return "updateFlight";
	}
	
	@RequestMapping(value = "/flight/update", method = RequestMethod.POST)
	private String updateFlightSubmit(@ModelAttribute FlightModel flight, Model model) {
		FlightModel archive = flightService.getFlightDetailById(flight.getId());
		
		archive.setDestination(flight.getDestination());
		archive.setFlightNumber(flight.getFlightNumber());
		archive.setOrigin(flight.getOrigin());
		archive.setTime(flight.getTime());
		
		flightService.addFlight(archive);
		model.addAttribute("title", "Update Flight");
		return "update";
	}
	
	@RequestMapping(value = "/flight/view")
	private String view(Model model) {
		List<FlightModel> archive = flightService.getAllFlight();
		
		model.addAttribute("flightList", archive);
		model.addAttribute("title", "View Flight");
		return "view-flight";
	}
}
