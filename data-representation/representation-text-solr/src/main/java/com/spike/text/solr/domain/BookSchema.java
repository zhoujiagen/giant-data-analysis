package com.spike.text.solr.domain;

import org.apache.solr.common.SolrDocument;

import com.spike.giantdataanalysis.commons.lang.StringUtils;

/**
 * Definition support of core book's schema
 * @author zhoujiagen
 * @see org.apache.solr.schema.FieldType
 */
public final class BookSchema {

  /**
   * <fieldType name="string" class="solr.StrField" sortMissingLast="true" />
   * @author zhoujiagen
   */
  public static enum FieldType {
    string("solr.StrField"), //
    text_en("solr.TextField");

    private String clazz;

    FieldType(String clazz) {
      this.clazz = clazz;
    }

    public String getClazz() {
      return clazz;
    }
  }

  /**
   * <pre>
   * Valid attributes for fields:
   *     &#64;param name: mandatory - the name for the field
   *     &#64;param type: mandatory - the name of a field type from the
   *     `types` fieldType section
   *     &#64;param indexed: true if this field should be indexed (searchable or sortable)
   *     &#64;param stored: true if this field should be retrievable
   *     &#64;param docValues: true if this field should have doc values. Doc values are
   *     useful for faceting, grouping, sorting and function queries. Although not
   *     required, doc values will make the index faster to load, more
   *     NRT-friendly and more memory-efficient. They however come with some
   *     limitations: they are currently only supported by StrField, UUIDField
   *     and all Trie*Fields, and depending on the field type, they might
   *     require the field to be single-valued, be required or have a default
   *     value (check the documentation of the field type you're interested in
   *     for more information)
   *     &#64;param multiValued: true if this field may contain multiple values per document
   *     &#64;param omitNorms: (expert) set to true to omit the norms associated with
   *     this field (this disables length normalization and index-time
   *     boosting for the field, and saves some memory). Only full-text
   *     fields or fields that need an index-time boost need norms.
   *     Norms are omitted for primitive (non-analyzed) types by default.
   *     &#64;param termVectors: [false] set to true to store the term vector for a
   *     given field.
   *     When using MoreLikeThis, fields used for similarity should be
   *     stored for best performance.
   *     &#64;param termPositions: Store position information with the term vector.
   *     This will increase storage costs.
   *     &#64;param termOffsets: Store offset information with the term vector. This
   *     will increase storage costs.
   *     &#64;param required: The field is required. It will throw an error if the
   *     value does not exist
   *     &#64;param default: a value that should be used if no value is specified
   *     when adding a document.
   * </pre>
   * 
   * @author zhoujiagen
   */
  public static enum Field {

    id(FieldType.string), //
    title(FieldType.string), //
    author(FieldType.string), //
    foreword(FieldType.text_en);

    private FieldType type;

    Field(FieldType type) {
      this.type = type;
    }

    public FieldType type() {
      return type;
    }
  }

  public static String renderBook(SolrDocument solrDocument) {
    StringBuilder sb = new StringBuilder();
    sb.append(StringUtils.NEWLINE);
    sb.append(StringUtils.REPEAT("=", 100));

    if (solrDocument != null) {
      for (Field field : Field.values()) {
        sb.append(field.name()).append("=");
        sb.append(solrDocument.get(field.name()));
        sb.append(StringUtils.NEWLINE);
      }
    }

    sb.append(StringUtils.NEWLINE);
    sb.append(StringUtils.REPEAT("=", 100));
    sb.append(StringUtils.NEWLINE);

    return sb.toString();
  }

}
