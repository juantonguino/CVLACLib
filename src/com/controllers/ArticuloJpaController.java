/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controllers;

import com.controllers.exceptions.IllegalOrphanException;
import com.controllers.exceptions.NonexistentEntityException;
import com.entities.Articulo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Revista;
import com.entities.TrabajoInvestigacion;
import com.entities.ArticuloAutor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class ArticuloJpaController implements Serializable {

    public ArticuloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Articulo articulo) {
        if (articulo.getArticuloAutorList() == null) {
            articulo.setArticuloAutorList(new ArrayList<ArticuloAutor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Revista revistaNombre = articulo.getRevistaNombre();
            if (revistaNombre != null) {
                revistaNombre = em.getReference(revistaNombre.getClass(), revistaNombre.getNombre());
                articulo.setRevistaNombre(revistaNombre);
            }
            TrabajoInvestigacion trabajoInvestigacionId = articulo.getTrabajoInvestigacionId();
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId = em.getReference(trabajoInvestigacionId.getClass(), trabajoInvestigacionId.getId());
                articulo.setTrabajoInvestigacionId(trabajoInvestigacionId);
            }
            List<ArticuloAutor> attachedArticuloAutorList = new ArrayList<ArticuloAutor>();
            for (ArticuloAutor articuloAutorListArticuloAutorToAttach : articulo.getArticuloAutorList()) {
                articuloAutorListArticuloAutorToAttach = em.getReference(articuloAutorListArticuloAutorToAttach.getClass(), articuloAutorListArticuloAutorToAttach.getId());
                attachedArticuloAutorList.add(articuloAutorListArticuloAutorToAttach);
            }
            articulo.setArticuloAutorList(attachedArticuloAutorList);
            em.persist(articulo);
            if (revistaNombre != null) {
                revistaNombre.getArticuloList().add(articulo);
                revistaNombre = em.merge(revistaNombre);
            }
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId.getArticuloList().add(articulo);
                trabajoInvestigacionId = em.merge(trabajoInvestigacionId);
            }
            for (ArticuloAutor articuloAutorListArticuloAutor : articulo.getArticuloAutorList()) {
                Articulo oldArticuloIdArticuloOfArticuloAutorListArticuloAutor = articuloAutorListArticuloAutor.getArticuloIdArticulo();
                articuloAutorListArticuloAutor.setArticuloIdArticulo(articulo);
                articuloAutorListArticuloAutor = em.merge(articuloAutorListArticuloAutor);
                if (oldArticuloIdArticuloOfArticuloAutorListArticuloAutor != null) {
                    oldArticuloIdArticuloOfArticuloAutorListArticuloAutor.getArticuloAutorList().remove(articuloAutorListArticuloAutor);
                    oldArticuloIdArticuloOfArticuloAutorListArticuloAutor = em.merge(oldArticuloIdArticuloOfArticuloAutorListArticuloAutor);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Articulo articulo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo persistentArticulo = em.find(Articulo.class, articulo.getIdArticulo());
            Revista revistaNombreOld = persistentArticulo.getRevistaNombre();
            Revista revistaNombreNew = articulo.getRevistaNombre();
            TrabajoInvestigacion trabajoInvestigacionIdOld = persistentArticulo.getTrabajoInvestigacionId();
            TrabajoInvestigacion trabajoInvestigacionIdNew = articulo.getTrabajoInvestigacionId();
            List<ArticuloAutor> articuloAutorListOld = persistentArticulo.getArticuloAutorList();
            List<ArticuloAutor> articuloAutorListNew = articulo.getArticuloAutorList();
            List<String> illegalOrphanMessages = null;
            for (ArticuloAutor articuloAutorListOldArticuloAutor : articuloAutorListOld) {
                if (!articuloAutorListNew.contains(articuloAutorListOldArticuloAutor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ArticuloAutor " + articuloAutorListOldArticuloAutor + " since its articuloIdArticulo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (revistaNombreNew != null) {
                revistaNombreNew = em.getReference(revistaNombreNew.getClass(), revistaNombreNew.getNombre());
                articulo.setRevistaNombre(revistaNombreNew);
            }
            if (trabajoInvestigacionIdNew != null) {
                trabajoInvestigacionIdNew = em.getReference(trabajoInvestigacionIdNew.getClass(), trabajoInvestigacionIdNew.getId());
                articulo.setTrabajoInvestigacionId(trabajoInvestigacionIdNew);
            }
            List<ArticuloAutor> attachedArticuloAutorListNew = new ArrayList<ArticuloAutor>();
            for (ArticuloAutor articuloAutorListNewArticuloAutorToAttach : articuloAutorListNew) {
                articuloAutorListNewArticuloAutorToAttach = em.getReference(articuloAutorListNewArticuloAutorToAttach.getClass(), articuloAutorListNewArticuloAutorToAttach.getId());
                attachedArticuloAutorListNew.add(articuloAutorListNewArticuloAutorToAttach);
            }
            articuloAutorListNew = attachedArticuloAutorListNew;
            articulo.setArticuloAutorList(articuloAutorListNew);
            articulo = em.merge(articulo);
            if (revistaNombreOld != null && !revistaNombreOld.equals(revistaNombreNew)) {
                revistaNombreOld.getArticuloList().remove(articulo);
                revistaNombreOld = em.merge(revistaNombreOld);
            }
            if (revistaNombreNew != null && !revistaNombreNew.equals(revistaNombreOld)) {
                revistaNombreNew.getArticuloList().add(articulo);
                revistaNombreNew = em.merge(revistaNombreNew);
            }
            if (trabajoInvestigacionIdOld != null && !trabajoInvestigacionIdOld.equals(trabajoInvestigacionIdNew)) {
                trabajoInvestigacionIdOld.getArticuloList().remove(articulo);
                trabajoInvestigacionIdOld = em.merge(trabajoInvestigacionIdOld);
            }
            if (trabajoInvestigacionIdNew != null && !trabajoInvestigacionIdNew.equals(trabajoInvestigacionIdOld)) {
                trabajoInvestigacionIdNew.getArticuloList().add(articulo);
                trabajoInvestigacionIdNew = em.merge(trabajoInvestigacionIdNew);
            }
            for (ArticuloAutor articuloAutorListNewArticuloAutor : articuloAutorListNew) {
                if (!articuloAutorListOld.contains(articuloAutorListNewArticuloAutor)) {
                    Articulo oldArticuloIdArticuloOfArticuloAutorListNewArticuloAutor = articuloAutorListNewArticuloAutor.getArticuloIdArticulo();
                    articuloAutorListNewArticuloAutor.setArticuloIdArticulo(articulo);
                    articuloAutorListNewArticuloAutor = em.merge(articuloAutorListNewArticuloAutor);
                    if (oldArticuloIdArticuloOfArticuloAutorListNewArticuloAutor != null && !oldArticuloIdArticuloOfArticuloAutorListNewArticuloAutor.equals(articulo)) {
                        oldArticuloIdArticuloOfArticuloAutorListNewArticuloAutor.getArticuloAutorList().remove(articuloAutorListNewArticuloAutor);
                        oldArticuloIdArticuloOfArticuloAutorListNewArticuloAutor = em.merge(oldArticuloIdArticuloOfArticuloAutorListNewArticuloAutor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = articulo.getIdArticulo();
                if (findArticulo(id) == null) {
                    throw new NonexistentEntityException("The articulo with id " + id + " no longer exists.");
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
            Articulo articulo;
            try {
                articulo = em.getReference(Articulo.class, id);
                articulo.getIdArticulo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articulo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ArticuloAutor> articuloAutorListOrphanCheck = articulo.getArticuloAutorList();
            for (ArticuloAutor articuloAutorListOrphanCheckArticuloAutor : articuloAutorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Articulo (" + articulo + ") cannot be destroyed since the ArticuloAutor " + articuloAutorListOrphanCheckArticuloAutor + " in its articuloAutorList field has a non-nullable articuloIdArticulo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Revista revistaNombre = articulo.getRevistaNombre();
            if (revistaNombre != null) {
                revistaNombre.getArticuloList().remove(articulo);
                revistaNombre = em.merge(revistaNombre);
            }
            TrabajoInvestigacion trabajoInvestigacionId = articulo.getTrabajoInvestigacionId();
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId.getArticuloList().remove(articulo);
                trabajoInvestigacionId = em.merge(trabajoInvestigacionId);
            }
            em.remove(articulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Articulo> findArticuloEntities() {
        return findArticuloEntities(true, -1, -1);
    }

    public List<Articulo> findArticuloEntities(int maxResults, int firstResult) {
        return findArticuloEntities(false, maxResults, firstResult);
    }

    private List<Articulo> findArticuloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Articulo.class));
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

    public Articulo findArticulo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Articulo.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticuloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Articulo> rt = cq.from(Articulo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
