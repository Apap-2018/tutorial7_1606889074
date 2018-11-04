package com.apap.tutorial7.service;

import com.apap.tutorial7.model.FlightModel;
import com.apap.tutorial7.repository.FlightDB;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FlightServiceImpl
 */
@Service
@Transactional
public class FlightServiceImpl implements FlightService {
	@Autowired
	private FlightDB flightDb;
	
	@Override
	public void addFlight(FlightModel flight) {
		flightDb.save(flight);
	}
	
	@Override
	public void deleteFlight(FlightModel flight) {
		flightDb.delete(flight);
	}

	@Override
	public FlightModel getFlightDetailById(long id) {
		return flightDb.findById(id);
	}

	@Override
	public List<FlightModel> getAllFlight() {
		
		return flightDb.findAll();
	}
}
