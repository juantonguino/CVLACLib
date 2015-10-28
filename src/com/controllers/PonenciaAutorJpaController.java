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
import com.entities.Ponencia;
import com.entities.PonenciaAutor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class PonenciaAutorJpaController implements Serializable {

    public PonenciaAutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PonenciaAutor ponenciaAutor) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Investigador investigadorIdentificacion = ponenciaAutor.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion = em.getReference(investigadorIdentificacion.getClass(), investigadorIdentificacion.getIdentificacion());
                ponenciaAutor.setInvestigadorIdentificacion(investigadorIdentificacion);
            }
            Ponencia ponenciaId = ponenciaAutor.getPonenciaId();
            if (ponenciaId != null) {
                ponenciaId = em.getReference(ponenciaId.getClass(), ponenciaId.getId());
                ponenciaAutor.setPonenciaId(ponenciaId);
            }
            em.persist(ponenciaAutor);
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getPonenciaAutorList().add(ponenciaAutor);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            if (ponenciaId != null) {
                ponenciaId.getPonenciaAutorList().add(ponenciaAutor);
                ponenciaId = em.merge(ponenciaId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PonenciaAutor ponenciaAutor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PonenciaAutor persistentPonenciaAutor = em.find(PonenciaAutor.class, ponenciaAutor.getId());
            Investigador investigadorIdentificacionOld = persistentPonenciaAutor.getInvestigadorIdentificacion();
            Investigador investigadorIdentificacionNew = ponenciaAutor.getInvestigadorIdentificacion();
            Ponencia ponenciaIdOld = persistentPonenciaAutor.getPonenciaId();
            Ponencia ponenciaIdNew = ponenciaAutor.getPonenciaId();
            if (investigadorIdentificacionNew != null) {
                investigadorIdentificacionNew = em.getReference(investigadorIdentificacionNew.getClass(), investigadorIdentificacionNew.getIdentificacion());
                ponenciaAutor.setInvestigadorIdentificacion(investigadorIdentificacionNew);
            }
            if (ponenciaIdNew != null) {
                ponenciaIdNew = em.getReference(ponenciaIdNew.getClass(), ponenciaIdNew.getId());
                ponenciaAutor.setPonenciaId(ponenciaIdNew);
            }
            ponenciaAutor = em.merge(ponenciaAutor);
            if (investigadorIdentificacionOld != null && !investigadorIdentificacionOld.equals(investigadorIdentificacionNew)) {
                investigadorIdentificacionOld.getPonenciaAutorList().remove(ponenciaAutor);
                investigadorIdentificacionOld = em.merge(investigadorIdentificacionOld);
            }
            if (investigadorIdentificacionNew != null && !investigadorIdentificacionNew.equals(investigadorIdentificacionOld)) {
                investigadorIdentificacionNew.getPonenciaAutorList().add(ponenciaAutor);
                investigadorIdentificacionNew = em.merge(investigadorIdentificacionNew);
            }
            if (ponenciaIdOld != null && !ponenciaIdOld.equals(ponenciaIdNew)) {
                ponenciaIdOld.getPonenciaAutorList().remove(ponenciaAutor);
                ponenciaIdOld = em.merge(ponenciaIdOld);
            }
            if (ponenciaIdNew != null && !ponenciaIdNew.equals(ponenciaIdOld)) {
                ponenciaIdNew.getPonenciaAutorList().add(ponenciaAutor);
                ponenciaIdNew = em.merge(ponenciaIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ponenciaAutor.getId();
                if (findPonenciaAutor(id) == null) {
                    throw new NonexistentEntityException("The ponenciaAutor with id " + id + " no longer exists.");
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
            PonenciaAutor ponenciaAutor;
            try {
                ponenciaAutor = em.getReference(PonenciaAutor.class, id);
                ponenciaAutor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ponenciaAutor with id " + id + " no longer exists.", enfe);
            }
            Investigador investigadorIdentificacion = ponenciaAutor.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getPonenciaAutorList().remove(ponenciaAutor);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            Ponencia ponenciaId = ponenciaAutor.getPonenciaId();
            if (ponenciaId != null) {
                ponenciaId.getPonenciaAutorList().remove(ponenciaAutor);
                ponenciaId = em.merge(ponenciaId);
            }
            em.remove(ponenciaAutor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PonenciaAutor> findPonenciaAutorEntities() {
        return findPonenciaAutorEntities(true, -1, -1);
    }

    public List<PonenciaAutor> findPonenciaAutorEntities(int maxResults, int firstResult) {
        return findPonenciaAutorEntities(false, maxResults, firstResult);
    }

    private List<PonenciaAutor> findPonenciaAutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PonenciaAutor.class));
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

    public PonenciaAutor findPonenciaAutor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PonenciaAutor.class, id);
        } finally {
            em.close();
        }
    }

    public int getPonenciaAutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PonenciaAutor> rt = cq.from(PonenciaAutor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
