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
import com.entities.GurpoInvestigacion;
import com.entities.InvestigadorTrabajoInvestigacion;
import java.util.ArrayList;
import java.util.List;
import com.entities.ArticuloAutor;
import com.entities.Investigador;
import com.entities.PonenciaAutor;
import com.entities.NivelFormacion;
import com.entities.LibroAutores;
import com.entities.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author juandiego
 */
public class InvestigadorJpaController implements Serializable {

    public InvestigadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Investigador investigador) throws PreexistingEntityException, Exception {
        if (investigador.getInvestigadorTrabajoInvestigacionList() == null) {
            investigador.setInvestigadorTrabajoInvestigacionList(new ArrayList<InvestigadorTrabajoInvestigacion>());
        }
        if (investigador.getArticuloAutorList() == null) {
            investigador.setArticuloAutorList(new ArrayList<ArticuloAutor>());
        }
        if (investigador.getPonenciaAutorList() == null) {
            investigador.setPonenciaAutorList(new ArrayList<PonenciaAutor>());
        }
        if (investigador.getNivelFormacionList() == null) {
            investigador.setNivelFormacionList(new ArrayList<NivelFormacion>());
        }
        if (investigador.getLibroAutoresList() == null) {
            investigador.setLibroAutoresList(new ArrayList<LibroAutores>());
        }
        if (investigador.getUsuarioList() == null) {
            investigador.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GurpoInvestigacion gurposInvestigacionNombre = investigador.getGurposInvestigacionNombre();
            if (gurposInvestigacionNombre != null) {
                gurposInvestigacionNombre = em.getReference(gurposInvestigacionNombre.getClass(), gurposInvestigacionNombre.getNombre());
                investigador.setGurposInvestigacionNombre(gurposInvestigacionNombre);
            }
            List<InvestigadorTrabajoInvestigacion> attachedInvestigadorTrabajoInvestigacionList = new ArrayList<InvestigadorTrabajoInvestigacion>();
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach : investigador.getInvestigadorTrabajoInvestigacionList()) {
                investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach = em.getReference(investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach.getClass(), investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach.getId());
                attachedInvestigadorTrabajoInvestigacionList.add(investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacionToAttach);
            }
            investigador.setInvestigadorTrabajoInvestigacionList(attachedInvestigadorTrabajoInvestigacionList);
            List<ArticuloAutor> attachedArticuloAutorList = new ArrayList<ArticuloAutor>();
            for (ArticuloAutor articuloAutorListArticuloAutorToAttach : investigador.getArticuloAutorList()) {
                articuloAutorListArticuloAutorToAttach = em.getReference(articuloAutorListArticuloAutorToAttach.getClass(), articuloAutorListArticuloAutorToAttach.getId());
                attachedArticuloAutorList.add(articuloAutorListArticuloAutorToAttach);
            }
            investigador.setArticuloAutorList(attachedArticuloAutorList);
            List<PonenciaAutor> attachedPonenciaAutorList = new ArrayList<PonenciaAutor>();
            for (PonenciaAutor ponenciaAutorListPonenciaAutorToAttach : investigador.getPonenciaAutorList()) {
                ponenciaAutorListPonenciaAutorToAttach = em.getReference(ponenciaAutorListPonenciaAutorToAttach.getClass(), ponenciaAutorListPonenciaAutorToAttach.getId());
                attachedPonenciaAutorList.add(ponenciaAutorListPonenciaAutorToAttach);
            }
            investigador.setPonenciaAutorList(attachedPonenciaAutorList);
            List<NivelFormacion> attachedNivelFormacionList = new ArrayList<NivelFormacion>();
            for (NivelFormacion nivelFormacionListNivelFormacionToAttach : investigador.getNivelFormacionList()) {
                nivelFormacionListNivelFormacionToAttach = em.getReference(nivelFormacionListNivelFormacionToAttach.getClass(), nivelFormacionListNivelFormacionToAttach.getIdEstudiosRealizados());
                attachedNivelFormacionList.add(nivelFormacionListNivelFormacionToAttach);
            }
            investigador.setNivelFormacionList(attachedNivelFormacionList);
            List<LibroAutores> attachedLibroAutoresList = new ArrayList<LibroAutores>();
            for (LibroAutores libroAutoresListLibroAutoresToAttach : investigador.getLibroAutoresList()) {
                libroAutoresListLibroAutoresToAttach = em.getReference(libroAutoresListLibroAutoresToAttach.getClass(), libroAutoresListLibroAutoresToAttach.getId());
                attachedLibroAutoresList.add(libroAutoresListLibroAutoresToAttach);
            }
            investigador.setLibroAutoresList(attachedLibroAutoresList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : investigador.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getNombre());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            investigador.setUsuarioList(attachedUsuarioList);
            em.persist(investigador);
            if (gurposInvestigacionNombre != null) {
                gurposInvestigacionNombre.getInvestigadorList().add(investigador);
                gurposInvestigacionNombre = em.merge(gurposInvestigacionNombre);
            }
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion : investigador.getInvestigadorTrabajoInvestigacionList()) {
                Investigador oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion = investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion.getInvestigadorIdentificacion();
                investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion.setInvestigadorIdentificacion(investigador);
                investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion = em.merge(investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion);
                if (oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion != null) {
                    oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion.getInvestigadorTrabajoInvestigacionList().remove(investigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion);
                    oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion = em.merge(oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListInvestigadorTrabajoInvestigacion);
                }
            }
            for (ArticuloAutor articuloAutorListArticuloAutor : investigador.getArticuloAutorList()) {
                Investigador oldInvestigadorIdentificacionOfArticuloAutorListArticuloAutor = articuloAutorListArticuloAutor.getInvestigadorIdentificacion();
                articuloAutorListArticuloAutor.setInvestigadorIdentificacion(investigador);
                articuloAutorListArticuloAutor = em.merge(articuloAutorListArticuloAutor);
                if (oldInvestigadorIdentificacionOfArticuloAutorListArticuloAutor != null) {
                    oldInvestigadorIdentificacionOfArticuloAutorListArticuloAutor.getArticuloAutorList().remove(articuloAutorListArticuloAutor);
                    oldInvestigadorIdentificacionOfArticuloAutorListArticuloAutor = em.merge(oldInvestigadorIdentificacionOfArticuloAutorListArticuloAutor);
                }
            }
            for (PonenciaAutor ponenciaAutorListPonenciaAutor : investigador.getPonenciaAutorList()) {
                Investigador oldInvestigadorIdentificacionOfPonenciaAutorListPonenciaAutor = ponenciaAutorListPonenciaAutor.getInvestigadorIdentificacion();
                ponenciaAutorListPonenciaAutor.setInvestigadorIdentificacion(investigador);
                ponenciaAutorListPonenciaAutor = em.merge(ponenciaAutorListPonenciaAutor);
                if (oldInvestigadorIdentificacionOfPonenciaAutorListPonenciaAutor != null) {
                    oldInvestigadorIdentificacionOfPonenciaAutorListPonenciaAutor.getPonenciaAutorList().remove(ponenciaAutorListPonenciaAutor);
                    oldInvestigadorIdentificacionOfPonenciaAutorListPonenciaAutor = em.merge(oldInvestigadorIdentificacionOfPonenciaAutorListPonenciaAutor);
                }
            }
            for (NivelFormacion nivelFormacionListNivelFormacion : investigador.getNivelFormacionList()) {
                Investigador oldInvestigadorIdentificacionOfNivelFormacionListNivelFormacion = nivelFormacionListNivelFormacion.getInvestigadorIdentificacion();
                nivelFormacionListNivelFormacion.setInvestigadorIdentificacion(investigador);
                nivelFormacionListNivelFormacion = em.merge(nivelFormacionListNivelFormacion);
                if (oldInvestigadorIdentificacionOfNivelFormacionListNivelFormacion != null) {
                    oldInvestigadorIdentificacionOfNivelFormacionListNivelFormacion.getNivelFormacionList().remove(nivelFormacionListNivelFormacion);
                    oldInvestigadorIdentificacionOfNivelFormacionListNivelFormacion = em.merge(oldInvestigadorIdentificacionOfNivelFormacionListNivelFormacion);
                }
            }
            for (LibroAutores libroAutoresListLibroAutores : investigador.getLibroAutoresList()) {
                Investigador oldInvestigadorIdentificacionOfLibroAutoresListLibroAutores = libroAutoresListLibroAutores.getInvestigadorIdentificacion();
                libroAutoresListLibroAutores.setInvestigadorIdentificacion(investigador);
                libroAutoresListLibroAutores = em.merge(libroAutoresListLibroAutores);
                if (oldInvestigadorIdentificacionOfLibroAutoresListLibroAutores != null) {
                    oldInvestigadorIdentificacionOfLibroAutoresListLibroAutores.getLibroAutoresList().remove(libroAutoresListLibroAutores);
                    oldInvestigadorIdentificacionOfLibroAutoresListLibroAutores = em.merge(oldInvestigadorIdentificacionOfLibroAutoresListLibroAutores);
                }
            }
            for (Usuario usuarioListUsuario : investigador.getUsuarioList()) {
                Investigador oldInvestigadorIdentificacionOfUsuarioListUsuario = usuarioListUsuario.getInvestigadorIdentificacion();
                usuarioListUsuario.setInvestigadorIdentificacion(investigador);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldInvestigadorIdentificacionOfUsuarioListUsuario != null) {
                    oldInvestigadorIdentificacionOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldInvestigadorIdentificacionOfUsuarioListUsuario = em.merge(oldInvestigadorIdentificacionOfUsuarioListUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInvestigador(investigador.getIdentificacion()) != null) {
                throw new PreexistingEntityException("Investigador " + investigador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Investigador investigador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Investigador persistentInvestigador = em.find(Investigador.class, investigador.getIdentificacion());
            GurpoInvestigacion gurposInvestigacionNombreOld = persistentInvestigador.getGurposInvestigacionNombre();
            GurpoInvestigacion gurposInvestigacionNombreNew = investigador.getGurposInvestigacionNombre();
            List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionListOld = persistentInvestigador.getInvestigadorTrabajoInvestigacionList();
            List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionListNew = investigador.getInvestigadorTrabajoInvestigacionList();
            List<ArticuloAutor> articuloAutorListOld = persistentInvestigador.getArticuloAutorList();
            List<ArticuloAutor> articuloAutorListNew = investigador.getArticuloAutorList();
            List<PonenciaAutor> ponenciaAutorListOld = persistentInvestigador.getPonenciaAutorList();
            List<PonenciaAutor> ponenciaAutorListNew = investigador.getPonenciaAutorList();
            List<NivelFormacion> nivelFormacionListOld = persistentInvestigador.getNivelFormacionList();
            List<NivelFormacion> nivelFormacionListNew = investigador.getNivelFormacionList();
            List<LibroAutores> libroAutoresListOld = persistentInvestigador.getLibroAutoresList();
            List<LibroAutores> libroAutoresListNew = investigador.getLibroAutoresList();
            List<Usuario> usuarioListOld = persistentInvestigador.getUsuarioList();
            List<Usuario> usuarioListNew = investigador.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListOldInvestigadorTrabajoInvestigacion : investigadorTrabajoInvestigacionListOld) {
                if (!investigadorTrabajoInvestigacionListNew.contains(investigadorTrabajoInvestigacionListOldInvestigadorTrabajoInvestigacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain InvestigadorTrabajoInvestigacion " + investigadorTrabajoInvestigacionListOldInvestigadorTrabajoInvestigacion + " since its investigadorIdentificacion field is not nullable.");
                }
            }
            for (ArticuloAutor articuloAutorListOldArticuloAutor : articuloAutorListOld) {
                if (!articuloAutorListNew.contains(articuloAutorListOldArticuloAutor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ArticuloAutor " + articuloAutorListOldArticuloAutor + " since its investigadorIdentificacion field is not nullable.");
                }
            }
            for (PonenciaAutor ponenciaAutorListOldPonenciaAutor : ponenciaAutorListOld) {
                if (!ponenciaAutorListNew.contains(ponenciaAutorListOldPonenciaAutor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PonenciaAutor " + ponenciaAutorListOldPonenciaAutor + " since its investigadorIdentificacion field is not nullable.");
                }
            }
            for (NivelFormacion nivelFormacionListOldNivelFormacion : nivelFormacionListOld) {
                if (!nivelFormacionListNew.contains(nivelFormacionListOldNivelFormacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain NivelFormacion " + nivelFormacionListOldNivelFormacion + " since its investigadorIdentificacion field is not nullable.");
                }
            }
            for (LibroAutores libroAutoresListOldLibroAutores : libroAutoresListOld) {
                if (!libroAutoresListNew.contains(libroAutoresListOldLibroAutores)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LibroAutores " + libroAutoresListOldLibroAutores + " since its investigadorIdentificacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (gurposInvestigacionNombreNew != null) {
                gurposInvestigacionNombreNew = em.getReference(gurposInvestigacionNombreNew.getClass(), gurposInvestigacionNombreNew.getNombre());
                investigador.setGurposInvestigacionNombre(gurposInvestigacionNombreNew);
            }
            List<InvestigadorTrabajoInvestigacion> attachedInvestigadorTrabajoInvestigacionListNew = new ArrayList<InvestigadorTrabajoInvestigacion>();
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach : investigadorTrabajoInvestigacionListNew) {
                investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach = em.getReference(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach.getClass(), investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach.getId());
                attachedInvestigadorTrabajoInvestigacionListNew.add(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacionToAttach);
            }
            investigadorTrabajoInvestigacionListNew = attachedInvestigadorTrabajoInvestigacionListNew;
            investigador.setInvestigadorTrabajoInvestigacionList(investigadorTrabajoInvestigacionListNew);
            List<ArticuloAutor> attachedArticuloAutorListNew = new ArrayList<ArticuloAutor>();
            for (ArticuloAutor articuloAutorListNewArticuloAutorToAttach : articuloAutorListNew) {
                articuloAutorListNewArticuloAutorToAttach = em.getReference(articuloAutorListNewArticuloAutorToAttach.getClass(), articuloAutorListNewArticuloAutorToAttach.getId());
                attachedArticuloAutorListNew.add(articuloAutorListNewArticuloAutorToAttach);
            }
            articuloAutorListNew = attachedArticuloAutorListNew;
            investigador.setArticuloAutorList(articuloAutorListNew);
            List<PonenciaAutor> attachedPonenciaAutorListNew = new ArrayList<PonenciaAutor>();
            for (PonenciaAutor ponenciaAutorListNewPonenciaAutorToAttach : ponenciaAutorListNew) {
                ponenciaAutorListNewPonenciaAutorToAttach = em.getReference(ponenciaAutorListNewPonenciaAutorToAttach.getClass(), ponenciaAutorListNewPonenciaAutorToAttach.getId());
                attachedPonenciaAutorListNew.add(ponenciaAutorListNewPonenciaAutorToAttach);
            }
            ponenciaAutorListNew = attachedPonenciaAutorListNew;
            investigador.setPonenciaAutorList(ponenciaAutorListNew);
            List<NivelFormacion> attachedNivelFormacionListNew = new ArrayList<NivelFormacion>();
            for (NivelFormacion nivelFormacionListNewNivelFormacionToAttach : nivelFormacionListNew) {
                nivelFormacionListNewNivelFormacionToAttach = em.getReference(nivelFormacionListNewNivelFormacionToAttach.getClass(), nivelFormacionListNewNivelFormacionToAttach.getIdEstudiosRealizados());
                attachedNivelFormacionListNew.add(nivelFormacionListNewNivelFormacionToAttach);
            }
            nivelFormacionListNew = attachedNivelFormacionListNew;
            investigador.setNivelFormacionList(nivelFormacionListNew);
            List<LibroAutores> attachedLibroAutoresListNew = new ArrayList<LibroAutores>();
            for (LibroAutores libroAutoresListNewLibroAutoresToAttach : libroAutoresListNew) {
                libroAutoresListNewLibroAutoresToAttach = em.getReference(libroAutoresListNewLibroAutoresToAttach.getClass(), libroAutoresListNewLibroAutoresToAttach.getId());
                attachedLibroAutoresListNew.add(libroAutoresListNewLibroAutoresToAttach);
            }
            libroAutoresListNew = attachedLibroAutoresListNew;
            investigador.setLibroAutoresList(libroAutoresListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getNombre());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            investigador.setUsuarioList(usuarioListNew);
            investigador = em.merge(investigador);
            if (gurposInvestigacionNombreOld != null && !gurposInvestigacionNombreOld.equals(gurposInvestigacionNombreNew)) {
                gurposInvestigacionNombreOld.getInvestigadorList().remove(investigador);
                gurposInvestigacionNombreOld = em.merge(gurposInvestigacionNombreOld);
            }
            if (gurposInvestigacionNombreNew != null && !gurposInvestigacionNombreNew.equals(gurposInvestigacionNombreOld)) {
                gurposInvestigacionNombreNew.getInvestigadorList().add(investigador);
                gurposInvestigacionNombreNew = em.merge(gurposInvestigacionNombreNew);
            }
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion : investigadorTrabajoInvestigacionListNew) {
                if (!investigadorTrabajoInvestigacionListOld.contains(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion)) {
                    Investigador oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion = investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion.getInvestigadorIdentificacion();
                    investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion.setInvestigadorIdentificacion(investigador);
                    investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion = em.merge(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion);
                    if (oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion != null && !oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion.equals(investigador)) {
                        oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion.getInvestigadorTrabajoInvestigacionList().remove(investigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion);
                        oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion = em.merge(oldInvestigadorIdentificacionOfInvestigadorTrabajoInvestigacionListNewInvestigadorTrabajoInvestigacion);
                    }
                }
            }
            for (ArticuloAutor articuloAutorListNewArticuloAutor : articuloAutorListNew) {
                if (!articuloAutorListOld.contains(articuloAutorListNewArticuloAutor)) {
                    Investigador oldInvestigadorIdentificacionOfArticuloAutorListNewArticuloAutor = articuloAutorListNewArticuloAutor.getInvestigadorIdentificacion();
                    articuloAutorListNewArticuloAutor.setInvestigadorIdentificacion(investigador);
                    articuloAutorListNewArticuloAutor = em.merge(articuloAutorListNewArticuloAutor);
                    if (oldInvestigadorIdentificacionOfArticuloAutorListNewArticuloAutor != null && !oldInvestigadorIdentificacionOfArticuloAutorListNewArticuloAutor.equals(investigador)) {
                        oldInvestigadorIdentificacionOfArticuloAutorListNewArticuloAutor.getArticuloAutorList().remove(articuloAutorListNewArticuloAutor);
                        oldInvestigadorIdentificacionOfArticuloAutorListNewArticuloAutor = em.merge(oldInvestigadorIdentificacionOfArticuloAutorListNewArticuloAutor);
                    }
                }
            }
            for (PonenciaAutor ponenciaAutorListNewPonenciaAutor : ponenciaAutorListNew) {
                if (!ponenciaAutorListOld.contains(ponenciaAutorListNewPonenciaAutor)) {
                    Investigador oldInvestigadorIdentificacionOfPonenciaAutorListNewPonenciaAutor = ponenciaAutorListNewPonenciaAutor.getInvestigadorIdentificacion();
                    ponenciaAutorListNewPonenciaAutor.setInvestigadorIdentificacion(investigador);
                    ponenciaAutorListNewPonenciaAutor = em.merge(ponenciaAutorListNewPonenciaAutor);
                    if (oldInvestigadorIdentificacionOfPonenciaAutorListNewPonenciaAutor != null && !oldInvestigadorIdentificacionOfPonenciaAutorListNewPonenciaAutor.equals(investigador)) {
                        oldInvestigadorIdentificacionOfPonenciaAutorListNewPonenciaAutor.getPonenciaAutorList().remove(ponenciaAutorListNewPonenciaAutor);
                        oldInvestigadorIdentificacionOfPonenciaAutorListNewPonenciaAutor = em.merge(oldInvestigadorIdentificacionOfPonenciaAutorListNewPonenciaAutor);
                    }
                }
            }
            for (NivelFormacion nivelFormacionListNewNivelFormacion : nivelFormacionListNew) {
                if (!nivelFormacionListOld.contains(nivelFormacionListNewNivelFormacion)) {
                    Investigador oldInvestigadorIdentificacionOfNivelFormacionListNewNivelFormacion = nivelFormacionListNewNivelFormacion.getInvestigadorIdentificacion();
                    nivelFormacionListNewNivelFormacion.setInvestigadorIdentificacion(investigador);
                    nivelFormacionListNewNivelFormacion = em.merge(nivelFormacionListNewNivelFormacion);
                    if (oldInvestigadorIdentificacionOfNivelFormacionListNewNivelFormacion != null && !oldInvestigadorIdentificacionOfNivelFormacionListNewNivelFormacion.equals(investigador)) {
                        oldInvestigadorIdentificacionOfNivelFormacionListNewNivelFormacion.getNivelFormacionList().remove(nivelFormacionListNewNivelFormacion);
                        oldInvestigadorIdentificacionOfNivelFormacionListNewNivelFormacion = em.merge(oldInvestigadorIdentificacionOfNivelFormacionListNewNivelFormacion);
                    }
                }
            }
            for (LibroAutores libroAutoresListNewLibroAutores : libroAutoresListNew) {
                if (!libroAutoresListOld.contains(libroAutoresListNewLibroAutores)) {
                    Investigador oldInvestigadorIdentificacionOfLibroAutoresListNewLibroAutores = libroAutoresListNewLibroAutores.getInvestigadorIdentificacion();
                    libroAutoresListNewLibroAutores.setInvestigadorIdentificacion(investigador);
                    libroAutoresListNewLibroAutores = em.merge(libroAutoresListNewLibroAutores);
                    if (oldInvestigadorIdentificacionOfLibroAutoresListNewLibroAutores != null && !oldInvestigadorIdentificacionOfLibroAutoresListNewLibroAutores.equals(investigador)) {
                        oldInvestigadorIdentificacionOfLibroAutoresListNewLibroAutores.getLibroAutoresList().remove(libroAutoresListNewLibroAutores);
                        oldInvestigadorIdentificacionOfLibroAutoresListNewLibroAutores = em.merge(oldInvestigadorIdentificacionOfLibroAutoresListNewLibroAutores);
                    }
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.setInvestigadorIdentificacion(null);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Investigador oldInvestigadorIdentificacionOfUsuarioListNewUsuario = usuarioListNewUsuario.getInvestigadorIdentificacion();
                    usuarioListNewUsuario.setInvestigadorIdentificacion(investigador);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldInvestigadorIdentificacionOfUsuarioListNewUsuario != null && !oldInvestigadorIdentificacionOfUsuarioListNewUsuario.equals(investigador)) {
                        oldInvestigadorIdentificacionOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldInvestigadorIdentificacionOfUsuarioListNewUsuario = em.merge(oldInvestigadorIdentificacionOfUsuarioListNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = investigador.getIdentificacion();
                if (findInvestigador(id) == null) {
                    throw new NonexistentEntityException("The investigador with id " + id + " no longer exists.");
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
            Investigador investigador;
            try {
                investigador = em.getReference(Investigador.class, id);
                investigador.getIdentificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The investigador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionListOrphanCheck = investigador.getInvestigadorTrabajoInvestigacionList();
            for (InvestigadorTrabajoInvestigacion investigadorTrabajoInvestigacionListOrphanCheckInvestigadorTrabajoInvestigacion : investigadorTrabajoInvestigacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Investigador (" + investigador + ") cannot be destroyed since the InvestigadorTrabajoInvestigacion " + investigadorTrabajoInvestigacionListOrphanCheckInvestigadorTrabajoInvestigacion + " in its investigadorTrabajoInvestigacionList field has a non-nullable investigadorIdentificacion field.");
            }
            List<ArticuloAutor> articuloAutorListOrphanCheck = investigador.getArticuloAutorList();
            for (ArticuloAutor articuloAutorListOrphanCheckArticuloAutor : articuloAutorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Investigador (" + investigador + ") cannot be destroyed since the ArticuloAutor " + articuloAutorListOrphanCheckArticuloAutor + " in its articuloAutorList field has a non-nullable investigadorIdentificacion field.");
            }
            List<PonenciaAutor> ponenciaAutorListOrphanCheck = investigador.getPonenciaAutorList();
            for (PonenciaAutor ponenciaAutorListOrphanCheckPonenciaAutor : ponenciaAutorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Investigador (" + investigador + ") cannot be destroyed since the PonenciaAutor " + ponenciaAutorListOrphanCheckPonenciaAutor + " in its ponenciaAutorList field has a non-nullable investigadorIdentificacion field.");
            }
            List<NivelFormacion> nivelFormacionListOrphanCheck = investigador.getNivelFormacionList();
            for (NivelFormacion nivelFormacionListOrphanCheckNivelFormacion : nivelFormacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Investigador (" + investigador + ") cannot be destroyed since the NivelFormacion " + nivelFormacionListOrphanCheckNivelFormacion + " in its nivelFormacionList field has a non-nullable investigadorIdentificacion field.");
            }
            List<LibroAutores> libroAutoresListOrphanCheck = investigador.getLibroAutoresList();
            for (LibroAutores libroAutoresListOrphanCheckLibroAutores : libroAutoresListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Investigador (" + investigador + ") cannot be destroyed since the LibroAutores " + libroAutoresListOrphanCheckLibroAutores + " in its libroAutoresList field has a non-nullable investigadorIdentificacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            GurpoInvestigacion gurposInvestigacionNombre = investigador.getGurposInvestigacionNombre();
            if (gurposInvestigacionNombre != null) {
                gurposInvestigacionNombre.getInvestigadorList().remove(investigador);
                gurposInvestigacionNombre = em.merge(gurposInvestigacionNombre);
            }
            List<Usuario> usuarioList = investigador.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.setInvestigadorIdentificacion(null);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            em.remove(investigador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Investigador> findInvestigadorEntities() {
        return findInvestigadorEntities(true, -1, -1);
    }

    public List<Investigador> findInvestigadorEntities(int maxResults, int firstResult) {
        return findInvestigadorEntities(false, maxResults, firstResult);
    }

    private List<Investigador> findInvestigadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Investigador.class));
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

    public Investigador findInvestigador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Investigador.class, id);
        } finally {
            em.close();
        }
    }

    public int getInvestigadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Investigador> rt = cq.from(Investigador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
