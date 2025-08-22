package edu.appstate.cs.cloud.restful.datastore;

import com.google.cloud.datastore.*;
import edu.appstate.cs.cloud.restful.models.Subject;
import edu.appstate.cs.cloud.restful.pubsub.Publish;
import edu.appstate.cs.cloud.restful.pubsub.Topics;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SubjectService {
    private Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final String ENTITY_KIND = "Subject";
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind(ENTITY_KIND);

    public Key createSubject(Subject subject) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity subjectEntity = Entity.newBuilder(key)
                .set(Subject.SUBJECT_NAME, subject.getSubjectName())
                .build();
        datastore.put(subjectEntity);

        new Publish
                .Builder()
                .forProject(Topics.PROJECT_ID)
                .toTopic(Topics.SUBJECT_CREATED)
                .sendId(key.getId())
                .build()
                .publish();

        return key;
    }

    public List<Subject> getAllSubjects() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(ENTITY_KIND)
                .build();
        Iterator<Entity> entities = datastore.run(query);
        return buildSubjects(entities);
    }

    public Subject getSubject(long subjectId) {
        // fetch a single Subject by ID (reads do not publish)
        Entity entity = datastore.get(keyFactory.newKey(subjectId));
        return entity == null
            ? null
            : entityToSubject(entity);
    }

    public void deleteSubject(long subjectId) {
        // delete from Datastore...
        datastore.delete(keyFactory.newKey(subjectId));

        // ...and notify subscribers
        new Publish
                .Builder()
                .forProject(Topics.PROJECT_ID)
                .toTopic(Topics.SUBJECT_DELETED)
                .sendId(subjectId)
                .build()
                .publish();
    }

    public void updateSubject(Subject subject) {
        // re‑build the existing entity with the new name
        Entity old = datastore.get(keyFactory.newKey(subject.getId()));
        Entity updated = Entity.newBuilder(old)
                .set(Subject.SUBJECT_NAME, subject.getSubjectName())
                .build();
        datastore.update(updated);

        new Publish
                .Builder()
                .forProject(Topics.PROJECT_ID)
                .toTopic(Topics.SUBJECT_UPDATED)
                .sendId(subject.getId())
                .build()
                .publish();
    }

    private List<Subject> buildSubjects(Iterator<Entity> entities) {
        List<Subject> subjects = new ArrayList<>();
        entities.forEachRemaining(e -> subjects.add(entityToSubject(e)));
        return subjects;
    }

    private Subject entityToSubject(Entity entity) {
        return new Subject.Builder()
                .withId(entity.getKey().getId())
                .withSubjectName(entity.getString(Subject.SUBJECT_NAME))
                .build();
    }
}
