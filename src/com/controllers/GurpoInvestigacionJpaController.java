/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controllers;

import com.controllers.exceptions.IllegalOrphanException;
import com.controllers.exceptions.NonexistentEntityException;
import com.controllers.exceptions.PreexistingEntityException;
import com.entities.GurpoInvestigacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.entities.Investigador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class GurpoInvestigacionJpaController implements Serializable {

    public GurpoInvestigacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GurpoInvestigacion gurpoInvestigacion) throws PreexistingEntityException, Exception {
        if (gurpoInvestigacion.getInvestigadorList() == null) {
            gurpoInvestigacion.setInvestigadorList(new ArrayList<Investigador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Investigador> attachedInvestigadorList = new ArrayList<Investigador>();
            for (Investigador investigadorListInvestigadorToAttach : gurpoInvestigacion.getInvestigadorList()) {
                investigadorListInvestigadorToAttach = em.getReference(investigadorListInvestigadorToAttach.getClass(), investigadorListInvestigadorToAttach.getIdentificacion());
                attachedInvestigadorList.add(investigadorListInvestigadorToAttach);
            }
            gurpoInvestigacion.setInvestigadorList(attachedInvestigadorList);
            em.persist(gurpoInvestigacion);
            for (Investigador investigadorListInvestigador : gurpoInvestigacion.getInvestigadorList()) {
                GurpoInvestigacion oldGurposInvestigacionNombreOfInvestigadorListInvestigador = investigadorListInvestigador.getGurposInvestigacionNombre();
                investigadorListInvestigador.setGurposInvestigacionNombre(gurpoInvestigacion);
                investigadorListInvestigador = em.merge(investigadorListInvestigador);
                if (oldGurposInvestigacionNombreOfInvestigadorListInvestigador != null) {
                    oldGurposInvestigacionNombreOfInvestigadorListInvestigador.getInvestigadorList().remove(investigadorListInvestigador);
                    oldGurposInvestigacionNombreOfInvestigadorListInvestigador = em.merge(oldGurposInvestigacionNombreOfInvestigadorListInvestigador);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGurpoInvestigacion(gurpoInvestigacion.getNombre()) != null) {
                throw new PreexistingEntityException("GurpoInvestigacion " + gurpoInvestigacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GurpoInvestigacion gurpoInvestigacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GurpoInvestigacion persistentGurpoInvestigacion = em.find(GurpoInvestigacion.class, gurpoInvestigacion.getNombre());
            List<Investigador> investigadorListOld = persistentGurpoInvestigacion.getInvestigadorList();
            List<Investigador> investigadorListNew = gurpoInvestigacion.getInvestigadorList();
            List<String> illegalOrphanMessages = null;
            for (Investigador investigadorListOldInvestigador : investigadorListOld) {
                if (!investigadorListNew.contains(investigadorListOldInvestigador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Investigador " + investigadorListOldInvestigador + " since its gurposInvestigacionNombre field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Investigador> attachedInvestigadorListNew = new ArrayList<Investigador>();
            for (Investigador investigadorListNewInvestigadorToAttach : investigadorListNew) {
                investigadorListNewInvestigadorToAttach = em.getReference(investigadorListNewInvestigadorToAttach.getClass(), investigadorListNewInvestigadorToAttach.getIdentificacion());
                attachedInvestigadorListNew.add(investigadorListNewInvestigadorToAttach);
            }
            investigadorListNew = attachedInvestigadorListNew;
            gurpoInvestigacion.setInvestigadorList(investigadorListNew);
            gurpoInvestigacion = em.merge(gurpoInvestigacion);
            for (Investigador investigadorListNewInvestigador : investigadorListNew) {
                if (!investigadorListOld.contains(investigadorListNewInvestigador)) {
                    GurpoInvestigacion oldGurposInvestigacionNombreOfInvestigadorListNewInvestigador = investigadorListNewInvestigador.getGurposInvestigacionNombre();
                    investigadorListNewInvestigador.setGurposInvestigacionNombre(gurpoInvestigacion);
                    investigadorListNewInvestigador = em.merge(investigadorListNewInvestigador);
                    if (oldGurposInvestigacionNombreOfInvestigadorListNewInvestigador != null && !oldGurposInvestigacionNombreOfInvestigadorListNewInvestigador.equals(gurpoInvestigacion)) {
                        oldGurposInvestigacionNombreOfInvestigadorListNewInvestigador.getInvestigadorList().remove(investigadorListNewInvestigador);
                        oldGurposInvestigacionNombreOfInvestigadorListNewInvestigador = em.merge(oldGurposInvestigacionNombreOfInvestigadorListNewInvestigador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = gurpoInvestigacion.getNombre();
                if (findGurpoInvestigacion(id) == null) {
                    throw new NonexistentEntityException("The gurpoInvestigacion with id " + id + " no longer exists.");
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
            GurpoInvestigacion gurpoInvestigacion;
            try {
                gurpoInvestigacion = em.getReference(GurpoInvestigacion.class, id);
                gurpoInvestigacion.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gurpoInvestigacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Investigador> investigadorListOrphanCheck = gurpoInvestigacion.getInvestigadorList();
            for (Investigador investigadorListOrphanCheckInvestigador : investigadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GurpoInvestigacion (" + gurpoInvestigacion + ") cannot be destroyed since the Investigador " + investigadorListOrphanCheckInvestigador + " in its investigadorList field has a non-nullable gurposInvestigacionNombre field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gurpoInvestigacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GurpoInvestigacion> findGurpoInvestigacionEntities() {
        return findGurpoInvestigacionEntities(true, -1, -1);
    }

    public List<GurpoInvestigacion> findGurpoInvestigacionEntities(int maxResults, int firstResult) {
        return findGurpoInvestigacionEntities(false, maxResults, firstResult);
    }

    private List<GurpoInvestigacion> findGurpoInvestigacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GurpoInvestigacion.class));
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

    public GurpoInvestigacion findGurpoInvestigacion(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GurpoInvestigacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getGurpoInvestigacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GurpoInvestigacion> rt = cq.from(GurpoInvestigacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
