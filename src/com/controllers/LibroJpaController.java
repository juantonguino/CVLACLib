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
import com.entities.Editorial;
import com.entities.Libro;
import com.entities.TrabajoInvestigacion;
import com.entities.LibroAutores;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class LibroJpaController implements Serializable {

    public LibroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libro libro) throws PreexistingEntityException, Exception {
        if (libro.getLibroAutoresList() == null) {
            libro.setLibroAutoresList(new ArrayList<LibroAutores>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Editorial editorialId = libro.getEditorialId();
            if (editorialId != null) {
                editorialId = em.getReference(editorialId.getClass(), editorialId.getId());
                libro.setEditorialId(editorialId);
            }
            TrabajoInvestigacion trabajoInvestigacionId = libro.getTrabajoInvestigacionId();
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId = em.getReference(trabajoInvestigacionId.getClass(), trabajoInvestigacionId.getId());
                libro.setTrabajoInvestigacionId(trabajoInvestigacionId);
            }
            List<LibroAutores> attachedLibroAutoresList = new ArrayList<LibroAutores>();
            for (LibroAutores libroAutoresListLibroAutoresToAttach : libro.getLibroAutoresList()) {
                libroAutoresListLibroAutoresToAttach = em.getReference(libroAutoresListLibroAutoresToAttach.getClass(), libroAutoresListLibroAutoresToAttach.getId());
                attachedLibroAutoresList.add(libroAutoresListLibroAutoresToAttach);
            }
            libro.setLibroAutoresList(attachedLibroAutoresList);
            em.persist(libro);
            if (editorialId != null) {
                editorialId.getLibroList().add(libro);
                editorialId = em.merge(editorialId);
            }
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId.getLibroList().add(libro);
                trabajoInvestigacionId = em.merge(trabajoInvestigacionId);
            }
            for (LibroAutores libroAutoresListLibroAutores : libro.getLibroAutoresList()) {
                Libro oldLibroIdOfLibroAutoresListLibroAutores = libroAutoresListLibroAutores.getLibroId();
                libroAutoresListLibroAutores.setLibroId(libro);
                libroAutoresListLibroAutores = em.merge(libroAutoresListLibroAutores);
                if (oldLibroIdOfLibroAutoresListLibroAutores != null) {
                    oldLibroIdOfLibroAutoresListLibroAutores.getLibroAutoresList().remove(libroAutoresListLibroAutores);
                    oldLibroIdOfLibroAutoresListLibroAutores = em.merge(oldLibroIdOfLibroAutoresListLibroAutores);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLibro(libro.getId()) != null) {
                throw new PreexistingEntityException("Libro " + libro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Libro libro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libro persistentLibro = em.find(Libro.class, libro.getId());
            Editorial editorialIdOld = persistentLibro.getEditorialId();
            Editorial editorialIdNew = libro.getEditorialId();
            TrabajoInvestigacion trabajoInvestigacionIdOld = persistentLibro.getTrabajoInvestigacionId();
            TrabajoInvestigacion trabajoInvestigacionIdNew = libro.getTrabajoInvestigacionId();
            List<LibroAutores> libroAutoresListOld = persistentLibro.getLibroAutoresList();
            List<LibroAutores> libroAutoresListNew = libro.getLibroAutoresList();
            List<String> illegalOrphanMessages = null;
            for (LibroAutores libroAutoresListOldLibroAutores : libroAutoresListOld) {
                if (!libroAutoresListNew.contains(libroAutoresListOldLibroAutores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LibroAutores " + libroAutoresListOldLibroAutores + " since its libroId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (editorialIdNew != null) {
                editorialIdNew = em.getReference(editorialIdNew.getClass(), editorialIdNew.getId());
                libro.setEditorialId(editorialIdNew);
            }
            if (trabajoInvestigacionIdNew != null) {
                trabajoInvestigacionIdNew = em.getReference(trabajoInvestigacionIdNew.getClass(), trabajoInvestigacionIdNew.getId());
                libro.setTrabajoInvestigacionId(trabajoInvestigacionIdNew);
            }
            List<LibroAutores> attachedLibroAutoresListNew = new ArrayList<LibroAutores>();
            for (LibroAutores libroAutoresListNewLibroAutoresToAttach : libroAutoresListNew) {
                libroAutoresListNewLibroAutoresToAttach = em.getReference(libroAutoresListNewLibroAutoresToAttach.getClass(), libroAutoresListNewLibroAutoresToAttach.getId());
                attachedLibroAutoresListNew.add(libroAutoresListNewLibroAutoresToAttach);
            }
            libroAutoresListNew = attachedLibroAutoresListNew;
            libro.setLibroAutoresList(libroAutoresListNew);
            libro = em.merge(libro);
            if (editorialIdOld != null && !editorialIdOld.equals(editorialIdNew)) {
                editorialIdOld.getLibroList().remove(libro);
                editorialIdOld = em.merge(editorialIdOld);
            }
            if (editorialIdNew != null && !editorialIdNew.equals(editorialIdOld)) {
                editorialIdNew.getLibroList().add(libro);
                editorialIdNew = em.merge(editorialIdNew);
            }
            if (trabajoInvestigacionIdOld != null && !trabajoInvestigacionIdOld.equals(trabajoInvestigacionIdNew)) {
                trabajoInvestigacionIdOld.getLibroList().remove(libro);
                trabajoInvestigacionIdOld = em.merge(trabajoInvestigacionIdOld);
            }
            if (trabajoInvestigacionIdNew != null && !trabajoInvestigacionIdNew.equals(trabajoInvestigacionIdOld)) {
                trabajoInvestigacionIdNew.getLibroList().add(libro);
                trabajoInvestigacionIdNew = em.merge(trabajoInvestigacionIdNew);
            }
            for (LibroAutores libroAutoresListNewLibroAutores : libroAutoresListNew) {
                if (!libroAutoresListOld.contains(libroAutoresListNewLibroAutores)) {
                    Libro oldLibroIdOfLibroAutoresListNewLibroAutores = libroAutoresListNewLibroAutores.getLibroId();
                    libroAutoresListNewLibroAutores.setLibroId(libro);
                    libroAutoresListNewLibroAutores = em.merge(libroAutoresListNewLibroAutores);
                    if (oldLibroIdOfLibroAutoresListNewLibroAutores != null && !oldLibroIdOfLibroAutoresListNewLibroAutores.equals(libro)) {
                        oldLibroIdOfLibroAutoresListNewLibroAutores.getLibroAutoresList().remove(libroAutoresListNewLibroAutores);
                        oldLibroIdOfLibroAutoresListNewLibroAutores = em.merge(oldLibroIdOfLibroAutoresListNewLibroAutores);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = libro.getId();
                if (findLibro(id) == null) {
                    throw new NonexistentEntityException("The libro with id " + id + " no longer exists.");
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
            Libro libro;
            try {
                libro = em.getReference(Libro.class, id);
                libro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LibroAutores> libroAutoresListOrphanCheck = libro.getLibroAutoresList();
            for (LibroAutores libroAutoresListOrphanCheckLibroAutores : libroAutoresListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Libro (" + libro + ") cannot be destroyed since the LibroAutores " + libroAutoresListOrphanCheckLibroAutores + " in its libroAutoresList field has a non-nullable libroId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Editorial editorialId = libro.getEditorialId();
            if (editorialId != null) {
                editorialId.getLibroList().remove(libro);
                editorialId = em.merge(editorialId);
            }
            TrabajoInvestigacion trabajoInvestigacionId = libro.getTrabajoInvestigacionId();
            if (trabajoInvestigacionId != null) {
                trabajoInvestigacionId.getLibroList().remove(libro);
                trabajoInvestigacionId = em.merge(trabajoInvestigacionId);
            }
            em.remove(libro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Libro> findLibroEntities() {
        return findLibroEntities(true, -1, -1);
    }

    public List<Libro> findLibroEntities(int maxResults, int firstResult) {
        return findLibroEntities(false, maxResults, firstResult);
    }

    private List<Libro> findLibroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libro.class));
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

    public Libro findLibro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Libro.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libro> rt = cq.from(Libro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
