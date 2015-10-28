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
import com.entities.InvestigadorTrabajoInvestigacion;
import com.entities.TrabajoInvestigacion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class InvestigadorTrabajoInvestigacionJpaController implements Serializable {

    public InvestigadorTrabajoInvestigacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Investigador investigadorIdentificacion = investigadorTrabajoInvestigacion.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion = em.getReference(investigadorIdentificacion.getClass(), investigadorIdentificacion.getIdentificacion());
                investigadorTrabajoInvestigacion.setInvestigadorIdentificacion(investigadorIdentificacion);
            }
            TrabajoInvestigacion trabajoInvestigacionId = investigadorTrabajoInvestigacion.getTrabajoInvestigacionId();
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId = em.getReference(trabajoInvestigacionId.getClass(), trabajoInvestigacionId.getId());
                investigadorTrabajoInvestigacion.setTrabajoInvestigacionId(trabajoInvestigacionId);
            }
            em.persist(investigadorTrabajoInvestigacion);
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getInvestigadorTrabajoInvestigacionList().add(investigadorTrabajoInvestigacion);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId.getInvestigadorTrabajoInvestigacionList().add(investigadorTrabajoInvestigacion);
                trabajoInvestigacionId = em.merge(trabajoInvestigacionId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InvestigadorTrabajoInvestigacion persistentInvestigadorTrabajoInvestigacion = em.find(InvestigadorTrabajoInvestigacion.class, investigadorTrabajoInvestigacion.getId());
            Investigador investigadorIdentificacionOld = persistentInvestigadorTrabajoInvestigacion.getInvestigadorIdentificacion();
            Investigador investigadorIdentificacionNew = investigadorTrabajoInvestigacion.getInvestigadorIdentificacion();
            TrabajoInvestigacion trabajoInvestigacionIdOld = persistentInvestigadorTrabajoInvestigacion.getTrabajoInvestigacionId();
            TrabajoInvestigacion trabajoInvestigacionIdNew = investigadorTrabajoInvestigacion.getTrabajoInvestigacionId();
            if (investigadorIdentificacionNew != null) {
                investigadorIdentificacionNew = em.getReference(investigadorIdentificacionNew.getClass(), investigadorIdentificacionNew.getIdentificacion());
                investigadorTrabajoInvestigacion.setInvestigadorIdentificacion(investigadorIdentificacionNew);
            }
            if (trabajoInvestigacionIdNew != null) {
                trabajoInvestigacionIdNew = em.getReference(trabajoInvestigacionIdNew.getClass(), trabajoInvestigacionIdNew.getId());
                investigadorTrabajoInvestigacion.setTrabajoInvestigacionId(trabajoInvestigacionIdNew);
            }
            investigadorTrabajoInvestigacion = em.merge(investigadorTrabajoInvestigacion);
            if (investigadorIdentificacionOld != null && !investigadorIdentificacionOld.equals(investigadorIdentificacionNew)) {
                investigadorIdentificacionOld.getInvestigadorTrabajoInvestigacionList().remove(investigadorTrabajoInvestigacion);
                investigadorIdentificacionOld = em.merge(investigadorIdentificacionOld);
            }
            if (investigadorIdentificacionNew != null && !investigadorIdentificacionNew.equals(investigadorIdentificacionOld)) {
                investigadorIdentificacionNew.getInvestigadorTrabajoInvestigacionList().add(investigadorTrabajoInvestigacion);
                investigadorIdentificacionNew = em.merge(investigadorIdentificacionNew);
            }
            if (trabajoInvestigacionIdOld != null && !trabajoInvestigacionIdOld.equals(trabajoInvestigacionIdNew)) {
                trabajoInvestigacionIdOld.getInvestigadorTrabajoInvestigacionList().remove(investigadorTrabajoInvestigacion);
                trabajoInvestigacionIdOld = em.merge(trabajoInvestigacionIdOld);
            }
            if (trabajoInvestigacionIdNew != null && !trabajoInvestigacionIdNew.equals(trabajoInvestigacionIdOld)) {
                trabajoInvestigacionIdNew.getInvestigadorTrabajoInvestigacionList().add(investigadorTrabajoInvestigacion);
                trabajoInvestigacionIdNew = em.merge(trabajoInvestigacionIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = investigadorTrabajoInvestigacion.getId();
                if (findInvestigadorTrabajoInvestigacion(id) == null) {
                    throw new NonexistentEntityException("The investigadorTrabajoInvestigacion with id " + id + " no longer exists.");
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
            InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacion;
            try {
                investigadorTrabajoInvestigacion = em.getReference(InvestigadorTrabajoInvestigacion.class, id);
                investigadorTrabajoInvestigacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The investigadorTrabajoInvestigacion with id " + id + " no longer exists.", enfe);
            }
            Investigador investigadorIdentificacion = investigadorTrabajoInvestigacion.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getInvestigadorTrabajoInvestigacionList().remove(investigadorTrabajoInvestigacion);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            TrabajoInvestigacion trabajoInvestigacionId = investigadorTrabajoInvestigacion.getTrabajoInvestigacionId();
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId.getInvestigadorTrabajoInvestigacionList().remove(investigadorTrabajoInvestigacion);
                trabajoInvestigacionId = em.merge(trabajoInvestigacionId);
            }
            em.remove(investigadorTrabajoInvestigacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InvestigadorTrabajoInvestigacion> findInvestigadorTrabajoInvestigacionEntities() {
        return findInvestigadorTrabajoInvestigacionEntities(true, -1, -1);
    }

    public List<InvestigadorTrabajoInvestigacion> findInvestigadorTrabajoInvestigacionEntities(int maxResults, int firstResult) {
        return findInvestigadorTrabajoInvestigacionEntities(false, maxResults, firstResult);
    }

    private List<InvestigadorTrabajoInvestigacion> findInvestigadorTrabajoInvestigacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InvestigadorTrabajoInvestigacion.class));
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

    public InvestigadorTrabajoInvestigacion findInvestigadorTrabajoInvestigacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InvestigadorTrabajoInvestigacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getInvestigadorTrabajoInvestigacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InvestigadorTrabajoInvestigacion> rt = cq.from(InvestigadorTrabajoInvestigacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
