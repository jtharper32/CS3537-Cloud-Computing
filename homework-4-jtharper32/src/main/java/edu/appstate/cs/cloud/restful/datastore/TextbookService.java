package edu.appstate.cs.cloud.restful.datastore;

import com.google.cloud.datastore.*;
import edu.appstate.cs.cloud.restful.models.Textbook;
import edu.appstate.cs.cloud.restful.pubsub.Publish;
import edu.appstate.cs.cloud.restful.pubsub.Topics;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TextbookService {
    private Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final String ENTITY_KIND = "Textbook";
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind(ENTITY_KIND);

    public Key createTextbook(Textbook textbook) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity textbookEntity = Entity.newBuilder(key)
                .set(Textbook.TITLE, textbook.getTitle())
                .set(Textbook.AUTHORS, textbook.getAuthors())
                .set(Textbook.SUBJECT, textbook.getSubject())
                .set(Textbook.PUBLISHER, textbook.getPublisher())
                .set(Textbook.YEAR, textbook.getYear())
                .set(Textbook.IMAGE_LINK, textbook.getImageLink())
                .build();
        datastore.put(textbookEntity);

        new Publish
                .Builder()
                .forProject(Topics.PROJECT_ID)
                .toTopic(Topics.TEXTBOOK_CREATED)
                .sendId(key.getId())
                .build()
                .publish();

        return key;
    }

    public List<Textbook> getAllTextbooks() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(ENTITY_KIND)
                .build();
        Iterator<Entity> entities = datastore.run(query);
        return buildTextbooks(entities);
    }

    public List<Textbook> getAllTextbooksForSubject(String subject) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(ENTITY_KIND)
                .setFilter(StructuredQuery.PropertyFilter.eq(Textbook.SUBJECT, subject))
                .build();
        Iterator<Entity> entities = datastore.run(query);
        return buildTextbooks(entities);
    }

    public Textbook getTextbook(long textbookId) {
        // fetch a single Textbook by ID (reads do not publish)
        Entity entity = datastore.get(keyFactory.newKey(textbookId));
        return entity == null
            ? null
            : entityToTextbook(entity);
    }

    public void deleteTextbook(long textbookId) {
        // delete from Datastore...
        datastore.delete(keyFactory.newKey(textbookId));

        // ...and notify subscribers
        new Publish
                .Builder()
                .forProject(Topics.PROJECT_ID)
                .toTopic(Topics.TEXTBOOK_DELETED)
                .sendId(textbookId)
                .build()
                .publish();
    }

    public void updateTextbook(Textbook textbook) {
        // re‑build the existing entity with all updated fields
        Entity old = datastore.get(keyFactory.newKey(textbook.getId()));
        Entity updated = Entity.newBuilder(old)
                .set(Textbook.TITLE, textbook.getTitle())
                .set(Textbook.AUTHORS, textbook.getAuthors())
                .set(Textbook.SUBJECT, textbook.getSubject())
                .set(Textbook.PUBLISHER, textbook.getPublisher())
                .set(Textbook.YEAR, textbook.getYear())
                .set(Textbook.IMAGE_LINK, textbook.getImageLink())
                .build();
        datastore.update(updated);

        new Publish
                .Builder()
                .forProject(Topics.PROJECT_ID)
                .toTopic(Topics.TEXTBOOK_UPDATED)
                .sendId(textbook.getId())
                .build()
                .publish();
    }

    private List<Textbook> buildTextbooks(Iterator<Entity> entities) {
        List<Textbook> textbooks = new ArrayList<>();
        entities.forEachRemaining(e -> textbooks.add(entityToTextbook(e)));
        return textbooks;
    }

    private Textbook entityToTextbook(Entity entity) {
        return new Textbook.Builder()
                .withId(entity.getKey().getId())
                .withTitle(entity.getString(Textbook.TITLE))
                .withAuthors(entity.getString(Textbook.AUTHORS))
                .withSubject(entity.getString(Textbook.SUBJECT))
                .withPublisher(entity.getString(Textbook.PUBLISHER))
                .withYear(entity.getLong(Textbook.YEAR))
                .withImageLink(entity.getString(Textbook.IMAGE_LINK))
                .build();
    }

    public boolean initSampleData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initSampleData'");
    }
}
