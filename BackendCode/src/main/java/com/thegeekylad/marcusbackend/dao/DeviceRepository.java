package com.thegeekylad.marcusbackend.dao;

import com.thegeekylad.marcusbackend.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
}
