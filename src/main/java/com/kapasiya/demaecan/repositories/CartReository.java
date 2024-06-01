package com.kapasiya.demaecan.repositories;

import com.kapasiya.demaecan.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartReository extends JpaRepository<Cart, Long>
{

}
