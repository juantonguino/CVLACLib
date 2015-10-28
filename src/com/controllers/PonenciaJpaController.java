/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controllers;

import com.controllers.exceptions.IllegalOrphanException;
import com.controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Evento;
import com.entities.Ponencia;
import com.entities.TrabajoInvestigacion;
import com.entities.PonenciaAutor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class PonenciaJpaController implements Serializable {

    public PonenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ponencia ponencia) {
        if (ponencia.getPonenciaAutorList() == null) {
            ponencia.setPonenciaAutorList(new ArrayList<PonenciaAutor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Evento eventoId = ponencia.getEventoId();
            if (eventoId != null) {
                eventoId = em.getReference(eventoId.getClass(), eventoId.getId());
                ponencia.setEventoId(eventoId);
            }
            TrabajoInvestigacion trabajoInvestigacionId = ponencia.getTrabajoInvestigacionId();
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId = em.getReference(trabajoInvestigacionId.getClass(), trabajoInvestigacionId.getId());
                ponencia.setTrabajoInvestigacionId(trabajoInvestigacionId);
            }
            List<PonenciaAutor> attachedPonenciaAutorList = new ArrayList<PonenciaAutor>();
            for (PonenciaAutor ponenciaAutorListPonenciaAutorToAttach : ponencia.getPonenciaAutorList()) {
                ponenciaAutorListPonenciaAutorToAttach = em.getReference(ponenciaAutorListPonenciaAutorToAttach.getClass(), ponenciaAutorListPonenciaAutorToAttach.getId());
                attachedPonenciaAutorList.add(ponenciaAutorListPonenciaAutorToAttach);
            }
            ponencia.setPonenciaAutorList(attachedPonenciaAutorList);
            em.persist(ponencia);
            if (eventoId != null) {
                eventoId.getPonenciaList().add(ponencia);
                eventoId = em.merge(eventoId);
            }
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId.getPonenciaList().add(ponencia);
                trabajoInvestigacionId = em.merge(trabajoInvestigacionId);
            }
            for (PonenciaAutor ponenciaAutorListPonenciaAutor : ponencia.getPonenciaAutorList()) {
                Ponencia oldPonenciaIdOfPonenciaAutorListPonenciaAutor = ponenciaAutorListPonenciaAutor.getPonenciaId();
                ponenciaAutorListPonenciaAutor.setPonenciaId(ponencia);
                ponenciaAutorListPonenciaAutor = em.merge(ponenciaAutorListPonenciaAutor);
                if (oldPonenciaIdOfPonenciaAutorListPonenciaAutor != null) {
                    oldPonenciaIdOfPonenciaAutorListPonenciaAutor.getPonenciaAutorList().remove(ponenciaAutorListPonenciaAutor);
                    oldPonenciaIdOfPonenciaAutorListPonenciaAutor = em.merge(oldPonenciaIdOfPonenciaAutorListPonenciaAutor);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ponencia ponencia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ponencia persistentPonencia = em.find(Ponencia.class, ponencia.getId());
            Evento eventoIdOld = persistentPonencia.getEventoId();
            Evento eventoIdNew = ponencia.getEventoId();
            TrabajoInvestigacion trabajoInvestigacionIdOld = persistentPonencia.getTrabajoInvestigacionId();
            TrabajoInvestigacion trabajoInvestigacionIdNew = ponencia.getTrabajoInvestigacionId();
            List<PonenciaAutor> ponenciaAutorListOld = persistentPonencia.getPonenciaAutorList();
            List<PonenciaAutor> ponenciaAutorListNew = ponencia.getPonenciaAutorList();
            List<String> illegalOrphanMessages = null;
            for (PonenciaAutor ponenciaAutorListOldPonenciaAutor : ponenciaAutorListOld) {
                if (!ponenciaAutorListNew.contains(ponenciaAutorListOldPonenciaAutor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PonenciaAutor " + ponenciaAutorListOldPonenciaAutor + " since its ponenciaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (eventoIdNew != null) {
                eventoIdNew = em.getReference(eventoIdNew.getClass(), eventoIdNew.getId());
                ponencia.setEventoId(eventoIdNew);
            }
            if (trabajoInvestigacionIdNew != null) {
                trabajoInvestigacionIdNew = em.getReference(trabajoInvestigacionIdNew.getClass(), trabajoInvestigacionIdNew.getId());
                ponencia.setTrabajoInvestigacionId(trabajoInvestigacionIdNew);
            }
            List<PonenciaAutor> attachedPonenciaAutorListNew = new ArrayList<PonenciaAutor>();
            for (PonenciaAutor ponenciaAutorListNewPonenciaAutorToAttach : ponenciaAutorListNew) {
                ponenciaAutorListNewPonenciaAutorToAttach = em.getReference(ponenciaAutorListNewPonenciaAutorToAttach.getClass(), ponenciaAutorListNewPonenciaAutorToAttach.getId());
                attachedPonenciaAutorListNew.add(ponenciaAutorListNewPonenciaAutorToAttach);
            }
            ponenciaAutorListNew = attachedPonenciaAutorListNew;
            ponencia.setPonenciaAutorList(ponenciaAutorListNew);
            ponencia = em.merge(ponencia);
            if (eventoIdOld != null && !eventoIdOld.equals(eventoIdNew)) {
                eventoIdOld.getPonenciaList().remove(ponencia);
                eventoIdOld = em.merge(eventoIdOld);
            }
            if (eventoIdNew != null && !eventoIdNew.equals(eventoIdOld)) {
                eventoIdNew.getPonenciaList().add(ponencia);
                eventoIdNew = em.merge(eventoIdNew);
            }
            if (trabajoInvestigacionIdOld != null && !trabajoInvestigacionIdOld.equals(trabajoInvestigacionIdNew)) {
                trabajoInvestigacionIdOld.getPonenciaList().remove(ponencia);
                trabajoInvestigacionIdOld = em.merge(trabajoInvestigacionIdOld);
            }
            if (trabajoInvestigacionIdNew != null && !trabajoInvestigacionIdNew.equals(trabajoInvestigacionIdOld)) {
                trabajoInvestigacionIdNew.getPonenciaList().add(ponencia);
                trabajoInvestigacionIdNew = em.merge(trabajoInvestigacionIdNew);
            }
            for (PonenciaAutor ponenciaAutorListNewPonenciaAutor : ponenciaAutorListNew) {
                if (!ponenciaAutorListOld.contains(ponenciaAutorListNewPonenciaAutor)) {
                    Ponencia oldPonenciaIdOfPonenciaAutorListNewPonenciaAutor = ponenciaAutorListNewPonenciaAutor.getPonenciaId();
                    ponenciaAutorListNewPonenciaAutor.setPonenciaId(ponencia);
                    ponenciaAutorListNewPonenciaAutor = em.merge(ponenciaAutorListNewPonenciaAutor);
                    if (oldPonenciaIdOfPonenciaAutorListNewPonenciaAutor != null && !oldPonenciaIdOfPonenciaAutorListNewPonenciaAutor.equals(ponencia)) {
                        oldPonenciaIdOfPonenciaAutorListNewPonenciaAutor.getPonenciaAutorList().remove(ponenciaAutorListNewPonenciaAutor);
                        oldPonenciaIdOfPonenciaAutorListNewPonenciaAutor = em.merge(oldPonenciaIdOfPonenciaAutorListNewPonenciaAutor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ponencia.getId();
                if (findPonencia(id) == null) {
                    throw new NonexistentEntityException("The ponencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ponencia ponencia;
            try {
                ponencia = em.getReference(Ponencia.class, id);
                ponencia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ponencia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PonenciaAutor> ponenciaAutorListOrphanCheck = ponencia.getPonenciaAutorList();
            for (PonenciaAutor ponenciaAutorListOrphanCheckPonenciaAutor : ponenciaAutorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ponencia (" + ponencia + ") cannot be destroyed since the PonenciaAutor " + ponenciaAutorListOrphanCheckPonenciaAutor + " in its ponenciaAutorList field has a non-nullable ponenciaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Evento eventoId = ponencia.getEventoId();
            if (eventoId != null) {
                eventoId.getPonenciaList().remove(ponencia);
                eventoId = em.merge(eventoId);
            }
            TrabajoInvestigacion trabajoInvestigacionId = ponencia.getTrabajoInvestigacionId();
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId.getPonenciaList().remove(ponencia);
                trabajoInvestigacionId = em.merge(trabajoInvestigacionId);
            }
            em.remove(ponencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ponencia> findPonenciaEntities() {
        return findPonenciaEntities(true, -1, -1);
    }

    public List<Ponencia> findPonenciaEntities(int maxResults, int firstResult) {
        return findPonenciaEntities(false, maxResults, firstResult);
    }

    private List<Ponencia> findPonenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ponencia.class));
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

    public Ponencia findPonencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ponencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getPonenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ponencia> rt = cq.from(Ponencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
