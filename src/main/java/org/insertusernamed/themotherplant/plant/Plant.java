package org.insertusernamed.themotherplant.plant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.insertusernamed.themotherplant.tag.Tag;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plants")
public class Plant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String commonName;

	@Column(columnDefinition = "TEXT")
	private String description;

	private String imageUrl;
	private BigDecimal price;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "plant_tags",
			joinColumns = @JoinColumn(name = "plant_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private Set<Tag> tags;
}
