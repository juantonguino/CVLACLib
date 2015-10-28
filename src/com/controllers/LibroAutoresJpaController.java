/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controllers;

import com.controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Investigador;
import com.entities.Libro;
import com.entities.LibroAutores;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class LibroAutoresJpaController implements Serializable {

    public LibroAutoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LibroAutores libroAutores) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Investigador investigadorIdentificacion = libroAutores.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion = em.getReference(investigadorIdentificacion.getClass(), investigadorIdentificacion.getIdentificacion());
                libroAutores.setInvestigadorIdentificacion(investigadorIdentificacion);
            }
            Libro libroId = libroAutores.getLibroId();
            if (libroId != null) {
                libroId = em.getReference(libroId.getClass(), libroId.getId());
                libroAutores.setLibroId(libroId);
            }
            em.persist(libroAutores);
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getLibroAutoresList().add(libroAutores);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            if (libroId != null) {
                libroId.getLibroAutoresList().add(libroAutores);
                libroId = em.merge(libroId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LibroAutores libroAutores) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LibroAutores persistentLibroAutores = em.find(LibroAutores.class, libroAutores.getId());
            Investigador investigadorIdentificacionOld = persistentLibroAutores.getInvestigadorIdentificacion();
            Investigador investigadorIdentificacionNew = libroAutores.getInvestigadorIdentificacion();
            Libro libroIdOld = persistentLibroAutores.getLibroId();
            Libro libroIdNew = libroAutores.getLibroId();
            if (investigadorIdentificacionNew != null) {
                investigadorIdentificacionNew = em.getReference(investigadorIdentificacionNew.getClass(), investigadorIdentificacionNew.getIdentificacion());
                libroAutores.setInvestigadorIdentificacion(investigadorIdentificacionNew);
            }
            if (libroIdNew != null) {
                libroIdNew = em.getReference(libroIdNew.getClass(), libroIdNew.getId());
                libroAutores.setLibroId(libroIdNew);
            }
            libroAutores = em.merge(libroAutores);
            if (investigadorIdentificacionOld != null && !investigadorIdentificacionOld.equals(investigadorIdentificacionNew)) {
                investigadorIdentificacionOld.getLibroAutoresList().remove(libroAutores);
                investigadorIdentificacionOld = em.merge(investigadorIdentificacionOld);
            }
            if (investigadorIdentificacionNew != null && !investigadorIdentificacionNew.equals(investigadorIdentificacionOld)) {
                investigadorIdentificacionNew.getLibroAutoresList().add(libroAutores);
                investigadorIdentificacionNew = em.merge(investigadorIdentificacionNew);
            }
            if (libroIdOld != null && !libroIdOld.equals(libroIdNew)) {
                libroIdOld.getLibroAutoresList().remove(libroAutores);
                libroIdOld = em.merge(libroIdOld);
            }
            if (libroIdNew != null && !libroIdNew.equals(libroIdOld)) {
                libroIdNew.getLibroAutoresList().add(libroAutores);
                libroIdNew = em.merge(libroIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = libroAutores.getId();
                if (findLibroAutores(id) == null) {
                    throw new NonexistentEntityException("The libroAutores with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LibroAutores libroAutores;
            try {
                libroAutores = em.getReference(LibroAutores.class, id);
                libroAutores.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libroAutores with id " + id + " no longer exists.", enfe);
            }
            Investigador investigadorIdentificacion = libroAutores.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getLibroAutoresList().remove(libroAutores);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            Libro libroId = libroAutores.getLibroId();
            if (libroId != null) {
                libroId.getLibroAutoresList().remove(libroAutores);
                libroId = em.merge(libroId);
            }
            em.remove(libroAutores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LibroAutores> findLibroAutoresEntities() {
        return findLibroAutoresEntities(true, -1, -1);
    }

    public List<LibroAutores> findLibroAutoresEntities(int maxResults, int firstResult) {
        return findLibroAutoresEntities(false, maxResults, firstResult);
    }

    private List<LibroAutores> findLibroAutoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LibroAutores.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public LibroAutores findLibroAutores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LibroAutores.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibroAutoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LibroAutores> rt = cq.from(LibroAutores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
