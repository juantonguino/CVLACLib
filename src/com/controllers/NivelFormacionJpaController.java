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
import com.entities.NivelFormacion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class NivelFormacionJpaController implements Serializable {

    public NivelFormacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NivelFormacion nivelFormacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Investigador investigadorIdentificacion = nivelFormacion.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion = em.getReference(investigadorIdentificacion.getClass(), investigadorIdentificacion.getIdentificacion());
                nivelFormacion.setInvestigadorIdentificacion(investigadorIdentificacion);
            }
            em.persist(nivelFormacion);
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getNivelFormacionList().add(nivelFormacion);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NivelFormacion nivelFormacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            NivelFormacion persistentNivelFormacion = em.find(NivelFormacion.class, nivelFormacion.getIdEstudiosRealizados());
            Investigador investigadorIdentificacionOld = persistentNivelFormacion.getInvestigadorIdentificacion();
            Investigador investigadorIdentificacionNew = nivelFormacion.getInvestigadorIdentificacion();
            if (investigadorIdentificacionNew != null) {
                investigadorIdentificacionNew = em.getReference(investigadorIdentificacionNew.getClass(), investigadorIdentificacionNew.getIdentificacion());
                nivelFormacion.setInvestigadorIdentificacion(investigadorIdentificacionNew);
            }
            nivelFormacion = em.merge(nivelFormacion);
            if (investigadorIdentificacionOld != null && !investigadorIdentificacionOld.equals(investigadorIdentificacionNew)) {
                investigadorIdentificacionOld.getNivelFormacionList().remove(nivelFormacion);
                investigadorIdentificacionOld = em.merge(investigadorIdentificacionOld);
            }
            if (investigadorIdentificacionNew != null && !investigadorIdentificacionNew.equals(investigadorIdentificacionOld)) {
                investigadorIdentificacionNew.getNivelFormacionList().add(nivelFormacion);
                investigadorIdentificacionNew = em.merge(investigadorIdentificacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nivelFormacion.getIdEstudiosRealizados();
                if (findNivelFormacion(id) == null) {
                    throw new NonexistentEntityException("The nivelFormacion with id " + id + " no longer exists.");
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
            NivelFormacion nivelFormacion;
            try {
                nivelFormacion = em.getReference(NivelFormacion.class, id);
                nivelFormacion.getIdEstudiosRealizados();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivelFormacion with id " + id + " no longer exists.", enfe);
            }
            Investigador investigadorIdentificacion = nivelFormacion.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getNivelFormacionList().remove(nivelFormacion);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            em.remove(nivelFormacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<NivelFormacion> findNivelFormacionEntities() {
        return findNivelFormacionEntities(true, -1, -1);
    }

    public List<NivelFormacion> findNivelFormacionEntities(int maxResults, int firstResult) {
        return findNivelFormacionEntities(false, maxResults, firstResult);
    }

    private List<NivelFormacion> findNivelFormacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NivelFormacion.class));
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

    public NivelFormacion findNivelFormacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NivelFormacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivelFormacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NivelFormacion> rt = cq.from(NivelFormacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
