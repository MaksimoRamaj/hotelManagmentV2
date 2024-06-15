package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.Room;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>
{
    List<Room> findAllByHotelId(Integer hotelId);

    @Query("SELECT r FROM Room r WHERE r.id NOT IN (" +
            "SELECT res.room.id FROM Reservation res " +
            "WHERE :checkInDate < res.checkOut AND :checkOutDate > res.checkIn) " +
            "AND r.kids = :kids AND r.adult = :adults")
    List<Room> findAvailableRooms(@Param("checkInDate") LocalDate checkInDate,
                                  @Param("checkOutDate") LocalDate checkOutDate,
                                  @Param("kids") int kids,
                                  @Param("adults") int adults);




    @Query("SELECT r FROM Room r WHERE r.id NOT IN (" +
            "SELECT r.id FROM Room r INNER JOIN Reservation rv ON r.id = rv.room.id " +
            "WHERE :newCheckIn < rv.checkOut AND :newCheckOut > rv.checkIn " +
            "AND r.adult = :adult AND r.kids = :kids) AND r.id = :roomId")
    Optional<Room> findAvailableRoom(
            @Param("newCheckIn") LocalDate newCheckIn,
            @Param("newCheckOut") LocalDate newCheckOut,
            @Param("adult") int adult,
            @Param("kids") int kids,
            @Param("roomId") int roomId);

}