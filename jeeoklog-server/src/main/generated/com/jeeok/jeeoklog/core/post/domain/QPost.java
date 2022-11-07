package com.jeeok.jeeoklog.core.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1389096752L;

    public static final QPost post = new QPost("post");

    public final com.jeeok.jeeoklog.common.entity.QBaseEntity _super = new com.jeeok.jeeoklog.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Integer> hits = createNumber("hits", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final StringPath title = createString("title");

    public QPost(String variable) {
        super(Post.class, forVariable(variable));
    }

    public QPost(Path<? extends Post> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPost(PathMetadata metadata) {
        super(Post.class, metadata);
    }

}

