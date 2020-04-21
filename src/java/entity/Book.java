package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;

@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String author;
    private String publishedYear;
    private Integer price;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateAdded;
    private boolean active;
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] textBookInBytes;
    private String coverUrl;

    public Book() {
    }

    public Book(String name,
            String author,
            String publishedYear,
            Integer price,
            Date dateAdded,
            boolean active,
            byte[] textBookInBytes,
            String coverUrl) {
        this.name = name;
        this.author = author;
        this.publishedYear = publishedYear;
        this.price = price;
        this.dateAdded = dateAdded;
        this.active = active;
        this.textBookInBytes = textBookInBytes;
        this.coverUrl = coverUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(String publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.author);
        hash = 29 * hash + Objects.hashCode(this.publishedYear);
        hash = 29 * hash + Objects.hashCode(this.price);
        hash = 29 * hash + Objects.hashCode(this.dateAdded);
        hash = 29 * hash + (this.active ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Book other = (Book) obj;
        if (this.active != other.active) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        if (!Objects.equals(this.publishedYear, other.publishedYear)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (!Objects.equals(this.dateAdded, other.dateAdded)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", name=" + name + ", author=" + author + ", publishedYear=" + publishedYear + ", price=" + price + ", dateAdded=" + dateAdded + ", active=" + active + '}';
    }

    public byte[] getTextBookInBytess() {
        return textBookInBytes;
    }

    public void setTextBookInBytes(byte[] textBookInBytes) {
        this.textBookInBytes = textBookInBytes;
    }

    public String getTextBookLimit(int countByte) {
        String fullText = new String(getTextBookInBytes());
        return fullText.substring(0, countByte);
    }

    public String getTextBookFull() {
        return new String(getTextBookInBytes());
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    private String getTextBookInBytes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
