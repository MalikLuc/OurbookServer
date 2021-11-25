package com.example.v2ourbook.repositories;


import com.example.v2ourbook.models.Licence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author norvin_klinkmann
 */

@Repository
public interface LicenceRepository extends JpaRepository<Licence, Long> {


}
