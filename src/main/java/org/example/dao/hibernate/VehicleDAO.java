package org.example.dao.hibernate;


import org.example.dao.IVehicleRepository;
import org.example.model.User;
import org.example.model.Vehicle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import java.util.Collection;


public class VehicleDAO implements IVehicleRepository {
    private static VehicleDAO instance;
    private final SessionFactory sessionFactory;

    // Private constructor to prevent instantiation outside of the class
    private VehicleDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Static method to get the instance of the singleton class
    public static synchronized VehicleDAO getInstance(SessionFactory sessionFactory) {
        if (instance == null) {
            instance = new VehicleDAO(sessionFactory);
        }
        return instance;
    }



    @Override
    public boolean rentVehicle(String plate, String login) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            User user = session.get(User.class, login);
            Vehicle vehicle = session.get(Vehicle.class, plate);

            if (user != null && vehicle != null && user.getVehicle() == null) {
                vehicle.setUser(user);
                vehicle.setRent(true);
                user.setVehicle(vehicle);

                session.saveOrUpdate(user);
                session.saveOrUpdate(vehicle);

                transaction.commit();
                return true;
            } else {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }


    @Override
    public boolean addVehicle(Vehicle vehicle) {
        Session session = null;
        Transaction transaction = null;
        boolean success=false;
        session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(vehicle);
            transaction.commit();
            success = true;

        }catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }

        return success;
    }
    @Override
    public boolean removeVehicle(String plate) {
        Session session = null;
        Transaction transaction = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, plate);
            if (vehicle != null && !vehicle.isRent()) {
                session.delete(vehicle);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return false;
    }

    @Override
    public Vehicle getVehicle(String plate) {
        Session session = sessionFactory.openSession();
        try {
            Vehicle vehicle = session.get(Vehicle.class, plate);
            return vehicle;
        } finally {
            session.close();
        }
    }

    public boolean returnVehicle(String plate,String login) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            User user = session.get(User.class, login);
            Vehicle vehicle = session.get(Vehicle.class, plate);

            if (user != null && vehicle != null && user.getVehicle() == vehicle && vehicle.isRent()) {
                vehicle.setRent(false);
                user.setVehicle(null);
                vehicle.setUser(null);

                session.saveOrUpdate(user);
                session.saveOrUpdate(vehicle);
                transaction.commit();
                return true;
            }else{
                if(transaction != null){
                    transaction.rollback();
                }

                return false;
            }
        }catch (RuntimeException e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return false;

    }

    @Override
    public Collection<Vehicle> getVehicles() {
        Collection<Vehicle> vehicles;
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            vehicles = session.createQuery("FROM Vehicle", Vehicle.class).getResultList();
            transaction.commit();
            return vehicles;
        }
        catch (RuntimeException e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return null;
    }
}
