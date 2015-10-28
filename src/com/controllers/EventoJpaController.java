/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controllers;

import com.controllers.exceptions.IllegalOrphanException;
import com.controllers.exceptions.NonexistentEntityException;
import com.entities.Evento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Ponencia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class EventoJpaController implements Serializable {

    public EventoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Evento evento) {
        if (evento.getPonenciaList() == null) {
            evento.setPonenciaList(new ArrayList<Ponencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Ponencia> attachedPonenciaList = new ArrayList<Ponencia>();
            for (Ponencia ponenciaListPonenciaToAttach : evento.getPonenciaList()) {
                ponenciaListPonenciaToAttach = em.getReference(ponenciaListPonenciaToAttach.getClass(), ponenciaListPonenciaToAttach.getId());
                attachedPonenciaList.add(ponenciaListPonenciaToAttach);
            }
            evento.setPonenciaList(attachedPonenciaList);
            em.persist(evento);
            for (Ponencia ponenciaListPonencia : evento.getPonenciaList()) {
                Evento oldEventoIdOfPonenciaListPonencia = ponenciaListPonencia.getEventoId();
                ponenciaListPonencia.setEventoId(evento);
                ponenciaListPonencia = em.merge(ponenciaListPonencia);
                if (oldEventoIdOfPonenciaListPonencia != null) {
                    oldEventoIdOfPonenciaListPonencia.getPonenciaList().remove(ponenciaListPonencia);
                    oldEventoIdOfPonenciaListPonencia = em.merge(oldEventoIdOfPonenciaListPonencia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Evento evento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Evento persistentEvento = em.find(Evento.class, evento.getId());
            List<Ponencia> ponenciaListOld = persistentEvento.getPonenciaList();
            List<Ponencia> ponenciaListNew = evento.getPonenciaList();
            List<String> illegalOrphanMessages = null;
            for (Ponencia ponenciaListOldPonencia : ponenciaListOld) {
                if (!ponenciaListNew.contains(ponenciaListOldPonencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ponencia " + ponenciaListOldPonencia + " since its eventoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Ponencia> attachedPonenciaListNew = new ArrayList<Ponencia>();
            for (Ponencia ponenciaListNewPonenciaToAttach : ponenciaListNew) {
                ponenciaListNewPonenciaToAttach = em.getReference(ponenciaListNewPonenciaToAttach.getClass(), ponenciaListNewPonenciaToAttach.getId());
                attachedPonenciaListNew.add(ponenciaListNewPonenciaToAttach);
            }
            ponenciaListNew = attachedPonenciaListNew;
            evento.setPonenciaList(ponenciaListNew);
            evento = em.merge(evento);
            for (Ponencia ponenciaListNewPonencia : ponenciaListNew) {
                if (!ponenciaListOld.contains(ponenciaListNewPonencia)) {
                    Evento oldEventoIdOfPonenciaListNewPonencia = ponenciaListNewPonencia.getEventoId();
                    ponenciaListNewPonencia.setEventoId(evento);
                    ponenciaListNewPonencia = em.merge(ponenciaListNewPonencia);
                    if (oldEventoIdOfPonenciaListNewPonencia != null && !oldEventoIdOfPonenciaListNewPonencia.equals(evento)) {
                        oldEventoIdOfPonenciaListNewPonencia.getPonenciaList().remove(ponenciaListNewPonencia);
                        oldEventoIdOfPonenciaListNewPonencia = em.merge(oldEventoIdOfPonenciaListNewPonencia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = evento.getId();
                if (findEvento(id) == null) {
                    throw new NonexistentEntityException("The evento with id " + id + " no longer exists.");
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
            Evento evento;
            try {
                evento = em.getReference(Evento.class, id);
                evento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The evento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ponencia> ponenciaListOrphanCheck = evento.getPonenciaList();
            for (Ponencia ponenciaListOrphanCheckPonencia : ponenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Evento (" + evento + ") cannot be destroyed since the Ponencia " + ponenciaListOrphanCheckPonencia + " in its ponenciaList field has a non-nullable eventoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(evento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Evento> findEventoEntities() {
        return findEventoEntities(true, -1, -1);
    }

    public List<Evento> findEventoEntities(int maxResults, int firstResult) {
        return findEventoEntities(false, maxResults, firstResult);
    }

    private List<Evento> findEventoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Evento.class));
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

    public Evento findEvento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Evento.class, id);
        } finally {
            em.close();
        }
    }

    public int getEventoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Evento> rt = cq.from(Evento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
