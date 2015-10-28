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
import com.entities.Libro;
import java.util.ArrayList;
import java.util.List;
import com.entities.InvestigadorTrabajoInvestigacion;
import com.entities.Ponencia;
import com.entities.Articulo;
import com.entities.TrabajoInvestigacion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class TrabajoInvestigacionJpaController implements Serializable {

    public TrabajoInvestigacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TrabajoInvestigacion trabajoInvestigacion) {
        if (trabajoInvestigacion.getLibroList() == null) {
            trabajoInvestigacion.setLibroList(new ArrayList<Libro>());
        }
        if (trabajoInvestigacion.getInvestigadorTrabajoInvestigacionList() == null) {
            trabajoInvestigacion.setInvestigadorTrabajoInvestigacionList(new ArrayList<InvestigadorTrabajoInvestigacion>());
        }
        if (trabajoInvestigacion.getPonenciaList() == null) {
            trabajoInvestigacion.setPonenciaList(new ArrayList<Ponencia>());
        }
        if (trabajoInvestigacion.getArticuloList() == null) {
            trabajoInvestigacion.setArticuloList(new ArrayList<Articulo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Libro> attachedLibroList = new ArrayList<Libro>();
            for (Libro libroListLibroToAttach : trabajoInvestigacion.getLibroList()) {
                libroListLibroToAttach = em.getReference(libroListLibroToAttach.getClass(), libroListLibroToAttach.getId());
                attachedLibroList.add(libroListLibroToAttach);
            }
            trabajoInvestigacion.setLibroList(attachedLibroList);
            List<InvestigadorTrabajoInvestigacion> attachedInvestigadorTrabajoInvestigacionList = new ArrayList<InvestigadorTrabajoInvestigacion>();
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach : trabajoInvestigacion.getInvestigadorTrabajoInvestigacionList()) {
                investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach = em.getReference(investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach.getClass(), investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach.getId());
                attachedInvestigadorTrabajoInvestigacionList.add(investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach);
            }
            trabajoInvestigacion.setInvestigadorTrabajoInvestigacionList(attachedInvestigadorTrabajoInvestigacionList);
            List<Ponencia> attachedPonenciaList = new ArrayList<Ponencia>();
            for (Ponencia ponenciaListPonenciaToAttach : trabajoInvestigacion.getPonenciaList()) {
                ponenciaListPonenciaToAttach = em.getReference(ponenciaListPonenciaToAttach.getClass(), ponenciaListPonenciaToAttach.getId());
                attachedPonenciaList.add(ponenciaListPonenciaToAttach);
            }
            trabajoInvestigacion.setPonenciaList(attachedPonenciaList);
            List<Articulo> attachedArticuloList = new ArrayList<Articulo>();
            for (Articulo articuloListArticuloToAttach : trabajoInvestigacion.getArticuloList()) {
                articuloListArticuloToAttach = em.getReference(articuloListArticuloToAttach.getClass(), articuloListArticuloToAttach.getIdArticulo());
                attachedArticuloList.add(articuloListArticuloToAttach);
            }
            trabajoInvestigacion.setArticuloList(attachedArticuloList);
            em.persist(trabajoInvestigacion);
            for (Libro libroListLibro : trabajoInvestigacion.getLibroList()) {
                TrabajoInvestigacion oldTrabajoInvestigacionIdOfLibroListLibro = libroListLibro.getTrabajoInvestigacionId();
                libroListLibro.setTrabajoInvestigacionId(trabajoInvestigacion);
                libroListLibro = em.merge(libroListLibro);
                if (oldTrabajoInvestigacionIdOfLibroListLibro != null) {
                    oldTrabajoInvestigacionIdOfLibroListLibro.getLibroList().remove(libroListLibro);
                    oldTrabajoInvestigacionIdOfLibroListLibro = em.merge(oldTrabajoInvestigacionIdOfLibroListLibro);
                }
            }
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion : trabajoInvestigacion.getInvestigadorTrabajoInvestigacionList()) {
                TrabajoInvestigacion oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion = investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion.getTrabajoInvestigacionId();
                investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion.setTrabajoInvestigacionId(trabajoInvestigacion);
                investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion = em.merge(investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion);
                if (oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion != null) {
                    oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion.getInvestigadorTrabajoInvestigacionList().remove(investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion);
                    oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion = em.merge(oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion);
                }
            }
            for (Ponencia ponenciaListPonencia : trabajoInvestigacion.getPonenciaList()) {
                TrabajoInvestigacion oldTrabajoInvestigacionIdOfPonenciaListPonencia = ponenciaListPonencia.getTrabajoInvestigacionId();
                ponenciaListPonencia.setTrabajoInvestigacionId(trabajoInvestigacion);
                ponenciaListPonencia = em.merge(ponenciaListPonencia);
                if (oldTrabajoInvestigacionIdOfPonenciaListPonencia != null) {
                    oldTrabajoInvestigacionIdOfPonenciaListPonencia.getPonenciaList().remove(ponenciaListPonencia);
                    oldTrabajoInvestigacionIdOfPonenciaListPonencia = em.merge(oldTrabajoInvestigacionIdOfPonenciaListPonencia);
                }
            }
            for (Articulo articuloListArticulo : trabajoInvestigacion.getArticuloList()) {
                TrabajoInvestigacion oldTrabajoInvestigacionIdOfArticuloListArticulo = articuloListArticulo.getTrabajoInvestigacionId();
                articuloListArticulo.setTrabajoInvestigacionId(trabajoInvestigacion);
                articuloListArticulo = em.merge(articuloListArticulo);
                if (oldTrabajoInvestigacionIdOfArticuloListArticulo != null) {
                    oldTrabajoInvestigacionIdOfArticuloListArticulo.getArticuloList().remove(articuloListArticulo);
                    oldTrabajoInvestigacionIdOfArticuloListArticulo = em.merge(oldTrabajoInvestigacionIdOfArticuloListArticulo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TrabajoInvestigacion trabajoInvestigacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TrabajoInvestigacion persistentTrabajoInvestigacion = em.find(TrabajoInvestigacion.class, trabajoInvestigacion.getId());
            List<Libro> libroListOld = persistentTrabajoInvestigacion.getLibroList();
            List<Libro> libroListNew = trabajoInvestigacion.getLibroList();
            List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionListOld = persistentTrabajoInvestigacion.getInvestigadorTrabajoInvestigacionList();
            List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionListNew = trabajoInvestigacion.getInvestigadorTrabajoInvestigacionList();
            List<Ponencia> ponenciaListOld = persistentTrabajoInvestigacion.getPonenciaList();
            List<Ponencia> ponenciaListNew = trabajoInvestigacion.getPonenciaList();
            List<Articulo> articuloListOld = persistentTrabajoInvestigacion.getArticuloList();
            List<Articulo> articuloListNew = trabajoInvestigacion.getArticuloList();
            List<String> illegalOrphanMessages = null;
            for (Libro libroListOldLibro : libroListOld) {
                if (!libroListNew.contains(libroListOldLibro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Libro " + libroListOldLibro + " since its trabajoInvestigacionId field is not nullable.");
                }
            }
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListOldInvestigadorTrabajoInvestigacion : investigadorTrabajoInvestigacionListOld) {
                if (!investigadorTrabajoInvestigacionListNew.contains(investigadorTrabajoInvestigacionListOldInvestigadorTrabajoInvestigacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InvestigadorTrabajoInvestigacion " + investigadorTrabajoInvestigacionListOldInvestigadorTrabajoInvestigacion + " since its trabajoInvestigacionId field is not nullable.");
                }
            }
            for (Ponencia ponenciaListOldPonencia : ponenciaListOld) {
                if (!ponenciaListNew.contains(ponenciaListOldPonencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ponencia " + ponenciaListOldPonencia + " since its trabajoInvestigacionId field is not nullable.");
                }
            }
            for (Articulo articuloListOldArticulo : articuloListOld) {
                if (!articuloListNew.contains(articuloListOldArticulo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Articulo " + articuloListOldArticulo + " since its trabajoInvestigacionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Libro> attachedLibroListNew = new ArrayList<Libro>();
            for (Libro libroListNewLibroToAttach : libroListNew) {
                libroListNewLibroToAttach = em.getReference(libroListNewLibroToAttach.getClass(), libroListNewLibroToAttach.getId());
                attachedLibroListNew.add(libroListNewLibroToAttach);
            }
            libroListNew = attachedLibroListNew;
            trabajoInvestigacion.setLibroList(libroListNew);
            List<InvestigadorTrabajoInvestigacion> attachedInvestigadorTrabajoInvestigacionListNew = new ArrayList<InvestigadorTrabajoInvestigacion>();
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach : investigadorTrabajoInvestigacionListNew) {
                investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach = em.getReference(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach.getClass(), investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach.getId());
                attachedInvestigadorTrabajoInvestigacionListNew.add(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach);
            }
            investigadorTrabajoInvestigacionListNew = attachedInvestigadorTrabajoInvestigacionListNew;
            trabajoInvestigacion.setInvestigadorTrabajoInvestigacionList(investigadorTrabajoInvestigacionListNew);
            List<Ponencia> attachedPonenciaListNew = new ArrayList<Ponencia>();
            for (Ponencia ponenciaListNewPonenciaToAttach : ponenciaListNew) {
                ponenciaListNewPonenciaToAttach = em.getReference(ponenciaListNewPonenciaToAttach.getClass(), ponenciaListNewPonenciaToAttach.getId());
                attachedPonenciaListNew.add(ponenciaListNewPonenciaToAttach);
            }
            ponenciaListNew = attachedPonenciaListNew;
            trabajoInvestigacion.setPonenciaList(ponenciaListNew);
            List<Articulo> attachedArticuloListNew = new ArrayList<Articulo>();
            for (Articulo articuloListNewArticuloToAttach : articuloListNew) {
                articuloListNewArticuloToAttach = em.getReference(articuloListNewArticuloToAttach.getClass(), articuloListNewArticuloToAttach.getIdArticulo());
                attachedArticuloListNew.add(articuloListNewArticuloToAttach);
            }
            articuloListNew = attachedArticuloListNew;
            trabajoInvestigacion.setArticuloList(articuloListNew);
            trabajoInvestigacion = em.merge(trabajoInvestigacion);
            for (Libro libroListNewLibro : libroListNew) {
                if (!libroListOld.contains(libroListNewLibro)) {
                    TrabajoInvestigacion oldTrabajoInvestigacionIdOfLibroListNewLibro = libroListNewLibro.getTrabajoInvestigacionId();
                    libroListNewLibro.setTrabajoInvestigacionId(trabajoInvestigacion);
                    libroListNewLibro = em.merge(libroListNewLibro);
                    if (oldTrabajoInvestigacionIdOfLibroListNewLibro != null && !oldTrabajoInvestigacionIdOfLibroListNewLibro.equals(trabajoInvestigacion)) {
                        oldTrabajoInvestigacionIdOfLibroListNewLibro.getLibroList().remove(libroListNewLibro);
                        oldTrabajoInvestigacionIdOfLibroListNewLibro = em.merge(oldTrabajoInvestigacionIdOfLibroListNewLibro);
                    }
                }
            }
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion : investigadorTrabajoInvestigacionListNew) {
                if (!investigadorTrabajoInvestigacionListOld.contains(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion)) {
                    TrabajoInvestigacion oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion = investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion.getTrabajoInvestigacionId();
                    investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion.setTrabajoInvestigacionId(trabajoInvestigacion);
                    investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion = em.merge(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion);
                    if (oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion != null && !oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion.equals(trabajoInvestigacion)) {
                        oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion.getInvestigadorTrabajoInvestigacionList().remove(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion);
                        oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion = em.merge(oldTrabajoInvestigacionIdOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion);
                    }
                }
            }
            for (Ponencia ponenciaListNewPonencia : ponenciaListNew) {
                if (!ponenciaListOld.contains(ponenciaListNewPonencia)) {
                    TrabajoInvestigacion oldTrabajoInvestigacionIdOfPonenciaListNewPonencia = ponenciaListNewPonencia.getTrabajoInvestigacionId();
                    ponenciaListNewPonencia.setTrabajoInvestigacionId(trabajoInvestigacion);
                    ponenciaListNewPonencia = em.merge(ponenciaListNewPonencia);
                    if (oldTrabajoInvestigacionIdOfPonenciaListNewPonencia != null && !oldTrabajoInvestigacionIdOfPonenciaListNewPonencia.equals(trabajoInvestigacion)) {
                        oldTrabajoInvestigacionIdOfPonenciaListNewPonencia.getPonenciaList().remove(ponenciaListNewPonencia);
                        oldTrabajoInvestigacionIdOfPonenciaListNewPonencia = em.merge(oldTrabajoInvestigacionIdOfPonenciaListNewPonencia);
                    }
                }
            }
            for (Articulo articuloListNewArticulo : articuloListNew) {
                if (!articuloListOld.contains(articuloListNewArticulo)) {
                    TrabajoInvestigacion oldTrabajoInvestigacionIdOfArticuloListNewArticulo = articuloListNewArticulo.getTrabajoInvestigacionId();
                    articuloListNewArticulo.setTrabajoInvestigacionId(trabajoInvestigacion);
                    articuloListNewArticulo = em.merge(articuloListNewArticulo);
                    if (oldTrabajoInvestigacionIdOfArticuloListNewArticulo != null && !oldTrabajoInvestigacionIdOfArticuloListNewArticulo.equals(trabajoInvestigacion)) {
                        oldTrabajoInvestigacionIdOfArticuloListNewArticulo.getArticuloList().remove(articuloListNewArticulo);
                        oldTrabajoInvestigacionIdOfArticuloListNewArticulo = em.merge(oldTrabajoInvestigacionIdOfArticuloListNewArticulo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = trabajoInvestigacion.getId();
                if (findTrabajoInvestigacion(id) == null) {
                    throw new NonexistentEntityException("The trabajoInvestigacion with id " + id + " no longer exists.");
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
            TrabajoInvestigacion trabajoInvestigacion;
            try {
                trabajoInvestigacion = em.getReference(TrabajoInvestigacion.class, id);
                trabajoInvestigacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trabajoInvestigacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Libro> libroListOrphanCheck = trabajoInvestigacion.getLibroList();
            for (Libro libroListOrphanCheckLibro : libroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TrabajoInvestigacion (" + trabajoInvestigacion + ") cannot be destroyed since the Libro " + libroListOrphanCheckLibro + " in its libroList field has a non-nullable trabajoInvestigacionId field.");
            }
            List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionListOrphanCheck = trabajoInvestigacion.getInvestigadorTrabajoInvestigacionList();
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListOrphanCheckInvestigadorTrabajoInvestigacion : investigadorTrabajoInvestigacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TrabajoInvestigacion (" + trabajoInvestigacion + ") cannot be destroyed since the InvestigadorTrabajoInvestigacion " + investigadorTrabajoInvestigacionListOrphanCheckInvestigadorTrabajoInvestigacion + " in its investigadorTrabajoInvestigacionList field has a non-nullable trabajoInvestigacionId field.");
            }
            List<Ponencia> ponenciaListOrphanCheck = trabajoInvestigacion.getPonenciaList();
            for (Ponencia ponenciaListOrphanCheckPonencia : ponenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TrabajoInvestigacion (" + trabajoInvestigacion + ") cannot be destroyed since the Ponencia " + ponenciaListOrphanCheckPonencia + " in its ponenciaList field has a non-nullable trabajoInvestigacionId field.");
            }
            List<Articulo> articuloListOrphanCheck = trabajoInvestigacion.getArticuloList();
            for (Articulo articuloListOrphanCheckArticulo : articuloListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TrabajoInvestigacion (" + trabajoInvestigacion + ") cannot be destroyed since the Articulo " + articuloListOrphanCheckArticulo + " in its articuloList field has a non-nullable trabajoInvestigacionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(trabajoInvestigacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TrabajoInvestigacion> findTrabajoInvestigacionEntities() {
        return findTrabajoInvestigacionEntities(true, -1, -1);
    }

    public List<TrabajoInvestigacion> findTrabajoInvestigacionEntities(int maxResults, int firstResult) {
        return findTrabajoInvestigacionEntities(false, maxResults, firstResult);
    }

    private List<TrabajoInvestigacion> findTrabajoInvestigacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TrabajoInvestigacion.class));
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

    public TrabajoInvestigacion findTrabajoInvestigacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TrabajoInvestigacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrabajoInvestigacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TrabajoInvestigacion> rt = cq.from(TrabajoInvestigacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
