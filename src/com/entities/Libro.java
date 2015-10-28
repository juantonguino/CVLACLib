/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juandiego
 */
@Entity
@Table(name = "libro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l"),
    @NamedQuery(name = "Libro.findById", query = "SELECT l FROM Libro l WHERE l.id = :id"),
    @NamedQuery(name = "Libro.findByTitulo", query = "SELECT l FROM Libro l WHERE l.titulo = :titulo"),
    @NamedQuery(name = "Libro.findByAnioPublicacin", query = "SELECT l FROM Libro l WHERE l.anioPublicacin = :anioPublicacin")})
public class Libro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @Column(name = "anio_publicacin")
    @Temporal(TemporalType.DATE)
    private Date anioPublicacin;
    @JoinColumn(name = "editorial_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Editorial editorialId;
    @JoinColumn(name = "trabajo_investigacion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TrabajoInvestigacion trabajoInvestigacionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroId")
    private List<LibroAutores> libroAutoresList;

    public Libro() {
    }

    public Libro(Integer id) {
        this.id = id;
    }

    public Libro(Integer id, String titulo, Date anioPublicacin) {
        this.id = id;
        this.titulo = titulo;
        this.anioPublicacin = anioPublicacin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getAnioPublicacin() {
        return anioPublicacin;
    }

    public void setAnioPublicacin(Date anioPublicacin) {
        this.anioPublicacin = anioPublicacin;
    }

    public Editorial getEditorialId() {
        return editorialId;
    }

    public void setEditorialId(Editorial editorialId) {
        this.editorialId = editorialId;
    }

    public TrabajoInvestigacion getTrabajoInvestigacionId() {
        return trabajoInvestigacionId;
    }

    public void setTrabajoInvestigacionId(TrabajoInvestigacion trabajoInvestigacionId) {
        this.trabajoInvestigacionId = trabajoInvestigacionId;
    }

    @XmlTransient
    public List<LibroAutores> getLibroAutoresList() {
        return libroAutoresList;
    }

    public void setLibroAutoresList(List<LibroAutores> libroAutoresList) {
        this.libroAutoresList = libroAutoresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libro)) {
            return false;
        }
        Libro other = (Libro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Libro[ id=" + id + " ]";
    }
    
}
