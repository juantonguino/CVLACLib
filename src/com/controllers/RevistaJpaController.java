/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controllers;

import com.controllers.exceptions.IllegalOrphanException;
import com.controllers.exceptions.NonexistentEntityException;
import com.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Articulo;
import com.entities.Revista;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class RevistaJpaController implements Serializable {

    public RevistaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Revista revista) throws PreexistingEntityException, Exception {
        if (revista.getArticuloList() == null) {
            revista.setArticuloList(new ArrayList<Articulo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Articulo> attachedArticuloList = new ArrayList<Articulo>();
            for (Articulo articuloListArticuloToAttach : revista.getArticuloList()) {
                articuloListArticuloToAttach = em.getReference(articuloListArticuloToAttach.getClass(), articuloListArticuloToAttach.getIdArticulo());
                attachedArticuloList.add(articuloListArticuloToAttach);
            }
            revista.setArticuloList(attachedArticuloList);
            em.persist(revista);
            for (Articulo articuloListArticulo : revista.getArticuloList()) {
                Revista oldRevistaNombreOfArticuloListArticulo = articuloListArticulo.getRevistaNombre();
                articuloListArticulo.setRevistaNombre(revista);
                articuloListArticulo = em.merge(articuloListArticulo);
                if (oldRevistaNombreOfArticuloListArticulo != null) {
                    oldRevistaNombreOfArticuloListArticulo.getArticuloList().remove(articuloListArticulo);
                    oldRevistaNombreOfArticuloListArticulo = em.merge(oldRevistaNombreOfArticuloListArticulo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRevista(revista.getNombre()) != null) {
                throw new PreexistingEntityException("Revista " + revista + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Revista revista) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Revista persistentRevista = em.find(Revista.class, revista.getNombre());
            List<Articulo> articuloListOld = persistentRevista.getArticuloList();
            List<Articulo> articuloListNew = revista.getArticuloList();
            List<String> illegalOrphanMessages = null;
            for (Articulo articuloListOldArticulo : articuloListOld) {
                if (!articuloListNew.contains(articuloListOldArticulo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Articulo " + articuloListOldArticulo + " since its revistaNombre field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Articulo> attachedArticuloListNew = new ArrayList<Articulo>();
            for (Articulo articuloListNewArticuloToAttach : articuloListNew) {
                articuloListNewArticuloToAttach = em.getReference(articuloListNewArticuloToAttach.getClass(), articuloListNewArticuloToAttach.getIdArticulo());
                attachedArticuloListNew.add(articuloListNewArticuloToAttach);
            }
            articuloListNew = attachedArticuloListNew;
            revista.setArticuloList(articuloListNew);
            revista = em.merge(revista);
            for (Articulo articuloListNewArticulo : articuloListNew) {
                if (!articuloListOld.contains(articuloListNewArticulo)) {
                    Revista oldRevistaNombreOfArticuloListNewArticulo = articuloListNewArticulo.getRevistaNombre();
                    articuloListNewArticulo.setRevistaNombre(revista);
                    articuloListNewArticulo = em.merge(articuloListNewArticulo);
                    if (oldRevistaNombreOfArticuloListNewArticulo != null && !oldRevistaNombreOfArticuloListNewArticulo.equals(revista)) {
                        oldRevistaNombreOfArticuloListNewArticulo.getArticuloList().remove(articuloListNewArticulo);
                        oldRevistaNombreOfArticuloListNewArticulo = em.merge(oldRevistaNombreOfArticuloListNewArticulo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = revista.getNombre();
                if (findRevista(id) == null) {
                    throw new NonexistentEntityException("The revista with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Revista revista;
            try {
                revista = em.getReference(Revista.class, id);
                revista.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The revista with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Articulo> articuloListOrphanCheck = revista.getArticuloList();
            for (Articulo articuloListOrphanCheckArticulo : articuloListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Revista (" + revista + ") cannot be destroyed since the Articulo " + articuloListOrphanCheckArticulo + " in its articuloList field has a non-nullable revistaNombre field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(revista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Revista> findRevistaEntities() {
        return findRevistaEntities(true, -1, -1);
    }

    public List<Revista> findRevistaEntities(int maxResults, int firstResult) {
        return findRevistaEntities(false, maxResults, firstResult);
    }

    private List<Revista> findRevistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Revista.class));
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

    public Revista findRevista(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Revista.class, id);
        } finally {
            em.close();
        }
    }

    public int getRevistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Revista> rt = cq.from(Revista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
