package com.aurionpro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.aurionpro.model.DeliveryPartner;
import com.aurionpro.util.SerializationUtil;

public class DeliveryPartnerService {
	private static DeliveryPartnerService instance;
	private static final String DELIVERY_PARTNERS_FILE = "delivery_partners.txt";

	private List<DeliveryPartner> deliveryPartners;
	private int deliveryPartnerIdCounter;
	private Random random; 

	private DeliveryPartnerService() {
		deliveryPartners = loadDeliveryPartners();
		deliveryPartnerIdCounter = 
				deliveryPartners.stream().mapToInt(DeliveryPartner::getId).max().orElse(0) + 1;
		random = new Random();
	}

	public static DeliveryPartnerService getInstance() {
		if (instance == null) {
			instance = new DeliveryPartnerService();
		}
		return instance;
	}

	private List<DeliveryPartner> loadDeliveryPartners() {
		List<DeliveryPartner> loadedPartners = SerializationUtil.deserialize(DELIVERY_PARTNERS_FILE);
		return loadedPartners != null ? loadedPartners : new ArrayList<>();
	}

	private void saveDeliveryPartners() {
		SerializationUtil.serialize(deliveryPartners, DELIVERY_PARTNERS_FILE);
	}


	public DeliveryPartner addDeliveryPartner(String name, String phoneNumber) {
		if (deliveryPartners.stream().anyMatch(dp -> dp.getName().equalsIgnoreCase(name))) {
			System.out.println("Error: Delivery partner with name '" + name + "' already exists.");
			return null;
		}

		int newId = deliveryPartnerIdCounter++;
		DeliveryPartner newPartner = new DeliveryPartner(newId, name, phoneNumber, true);
		deliveryPartners.add(newPartner);
		saveDeliveryPartners();
		System.out.println("Delivery partner added: " + newPartner.getName() + " with ID " + newPartner.getId());
		return newPartner;
	}

	public List<DeliveryPartner> getAllDeliveryPartners() {
		return new ArrayList<>(deliveryPartners); 
	}

	public Optional<DeliveryPartner> getDeliveryPartnerById(int id) {
		return deliveryPartners.stream().filter(dp -> dp.getId() == id).findFirst();
	}

	public DeliveryPartner assignRandomDeliveryPartner() {
	    List<DeliveryPartner> availablePartners = deliveryPartners.stream()
	            .filter(DeliveryPartner::isAvailabilityStatus) 
	            .collect(Collectors.toList());

	    if (availablePartners.isEmpty()) {
	        System.out.println("No delivery partners available for assignment.");
	        return null;
	    }

	    DeliveryPartner assignedPartner = availablePartners.get(random.nextInt(availablePartners.size()));
	    assignedPartner.setAvailabilityStatus(false); 
	    saveDeliveryPartners(); 
	    System.out.println("Delivery partner " + assignedPartner.getName() + " (ID: " + assignedPartner.getId() + ") assigned and status set to unavailable.");
	    return assignedPartner;
	}
	
	public boolean updateDeliveryPartnerStatus(int id, boolean newStatus) {
	    Optional<DeliveryPartner> optionalPartner = deliveryPartners.stream().filter(dp -> dp.getId() == id).findFirst();
	    if (optionalPartner.isPresent()) {
	        DeliveryPartner partner = optionalPartner.get();
	        if (partner.isAvailabilityStatus() != newStatus) { 
	            partner.setAvailabilityStatus(newStatus);
	            saveDeliveryPartners(); 
	            System.out.println("Delivery partner ID " + id + " availability updated to: " + (newStatus ? "Available" : "Engaged"));
	        } else {
	            System.out.println("Delivery partner ID " + id + " is already " + (newStatus ? "Available" : "Engaged") + ".");
	        }
	        return true;
	    }
	    System.out.println("Error: Delivery partner with ID " + id + " not found.");
	    return false;
	}

	// update delete delivery partner (futuer scope)
}