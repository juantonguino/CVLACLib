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
import com.entities.Articulo;
import com.entities.ArticuloAutor;
import com.entities.Investigador;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class ArticuloAutorJpaController implements Serializable {

    public ArticuloAutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ArticuloAutor articuloAutor) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo articuloIdArticulo = articuloAutor.getArticuloIdArticulo();
            if (articuloIdArticulo != null) {
                articuloIdArticulo = em.getReference(articuloIdArticulo.getClass(), articuloIdArticulo.getIdArticulo());
                articuloAutor.setArticuloIdArticulo(articuloIdArticulo);
            }
            Investigador investigadorIdentificacion = articuloAutor.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion = em.getReference(investigadorIdentificacion.getClass(), investigadorIdentificacion.getIdentificacion());
                articuloAutor.setInvestigadorIdentificacion(investigadorIdentificacion);
            }
            em.persist(articuloAutor);
            if (articuloIdArticulo != null) {
                articuloIdArticulo.getArticuloAutorList().add(articuloAutor);
                articuloIdArticulo = em.merge(articuloIdArticulo);
            }
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getArticuloAutorList().add(articuloAutor);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ArticuloAutor articuloAutor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ArticuloAutor persistentArticuloAutor = em.find(ArticuloAutor.class, articuloAutor.getId());
            Articulo articuloIdArticuloOld = persistentArticuloAutor.getArticuloIdArticulo();
            Articulo articuloIdArticuloNew = articuloAutor.getArticuloIdArticulo();
            Investigador investigadorIdentificacionOld = persistentArticuloAutor.getInvestigadorIdentificacion();
            Investigador investigadorIdentificacionNew = articuloAutor.getInvestigadorIdentificacion();
            if (articuloIdArticuloNew != null) {
                articuloIdArticuloNew = em.getReference(articuloIdArticuloNew.getClass(), articuloIdArticuloNew.getIdArticulo());
                articuloAutor.setArticuloIdArticulo(articuloIdArticuloNew);
            }
            if (investigadorIdentificacionNew != null) {
                investigadorIdentificacionNew = em.getReference(investigadorIdentificacionNew.getClass(), investigadorIdentificacionNew.getIdentificacion());
                articuloAutor.setInvestigadorIdentificacion(investigadorIdentificacionNew);
            }
            articuloAutor = em.merge(articuloAutor);
            if (articuloIdArticuloOld != null && !articuloIdArticuloOld.equals(articuloIdArticuloNew)) {
                articuloIdArticuloOld.getArticuloAutorList().remove(articuloAutor);
                articuloIdArticuloOld = em.merge(articuloIdArticuloOld);
            }
            if (articuloIdArticuloNew != null && !articuloIdArticuloNew.equals(articuloIdArticuloOld)) {
                articuloIdArticuloNew.getArticuloAutorList().add(articuloAutor);
                articuloIdArticuloNew = em.merge(articuloIdArticuloNew);
            }
            if (investigadorIdentificacionOld != null && !investigadorIdentificacionOld.equals(investigadorIdentificacionNew)) {
                investigadorIdentificacionOld.getArticuloAutorList().remove(articuloAutor);
                investigadorIdentificacionOld = em.merge(investigadorIdentificacionOld);
            }
            if (investigadorIdentificacionNew != null && !investigadorIdentificacionNew.equals(investigadorIdentificacionOld)) {
                investigadorIdentificacionNew.getArticuloAutorList().add(articuloAutor);
                investigadorIdentificacionNew = em.merge(investigadorIdentificacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = articuloAutor.getId();
                if (findArticuloAutor(id) == null) {
                    throw new NonexistentEntityException("The articuloAutor with id " + id + " no longer exists.");
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
            ArticuloAutor articuloAutor;
            try {
                articuloAutor = em.getReference(ArticuloAutor.class, id);
                articuloAutor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articuloAutor with id " + id + " no longer exists.", enfe);
            }
            Articulo articuloIdArticulo = articuloAutor.getArticuloIdArticulo();
            if (articuloIdArticulo != null) {
                articuloIdArticulo.getArticuloAutorList().remove(articuloAutor);
                articuloIdArticulo = em.merge(articuloIdArticulo);
            }
            Investigador investigadorIdentificacion = articuloAutor.getInvestigadorIdentificacion();
            if (investigadorIdentificacion != null) {
                investigadorIdentificacion.getArticuloAutorList().remove(articuloAutor);
                investigadorIdentificacion = em.merge(investigadorIdentificacion);
            }
            em.remove(articuloAutor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ArticuloAutor> findArticuloAutorEntities() {
        return findArticuloAutorEntities(true, -1, -1);
    }

    public List<ArticuloAutor> findArticuloAutorEntities(int maxResults, int firstResult) {
        return findArticuloAutorEntities(false, maxResults, firstResult);
    }

    private List<ArticuloAutor> findArticuloAutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ArticuloAutor.class));
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

    public ArticuloAutor findArticuloAutor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ArticuloAutor.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticuloAutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ArticuloAutor> rt = cq.from(ArticuloAutor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
