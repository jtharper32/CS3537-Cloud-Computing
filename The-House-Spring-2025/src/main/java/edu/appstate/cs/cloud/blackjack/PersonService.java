package src.main.java.edu.appstate.cs.cloud.blackjack;

import com.google.cloud.datastore.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonService {
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final String ENTITY_KIND = "Person";
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind(ENTITY_KIND);

    public Key createPerson(Person person) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity personEntity = Entity.newBuilder(key)
                .set(Person.NAME, person.getName())
                .set(Person.BALANCE, person.getBalance())
                .build();
        datastore.put(personEntity);
        return key;
    }

    public Person getPersonByName(String name) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(ENTITY_KIND)
                .setFilter(StructuredQuery.PropertyFilter.eq(Person.NAME, name))
                .build();
        Iterator<Entity> entities = datastore.run(query);
        if (entities.hasNext()) {
            return entityToPerson(entities.next());
        }
        return null;
    }

    public void updatePerson(Person person) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(ENTITY_KIND)
                .setFilter(StructuredQuery.PropertyFilter.eq(Person.NAME, person.getName()))
                .build();
        Iterator<Entity> entities = datastore.run(query);
        if (entities.hasNext()) {
            Entity entity = entities.next();
            Entity updatedEntity = Entity.newBuilder(entity.getKey())
                    .set(Person.NAME, person.getName())
                    .set(Person.BALANCE, person.getBalance())
                    .build();
            datastore.put(updatedEntity);
        }
    }

    public List<Person> getAllPersons() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(ENTITY_KIND)
                .build();
        Iterator<Entity> entities = datastore.run(query);
        return buildPersons(entities);
    }

    private List<Person> buildPersons(Iterator<Entity> entities) {
        List<Person> persons = new ArrayList<>();
        entities.forEachRemaining(entity -> persons.add(entityToPerson(entity)));
        return persons;
    }

    private Person entityToPerson(Entity entity) {
        return new Person.Builder()
                .withName(entity.getString(Person.NAME))
                .withBalance(entity.getDouble(Person.BALANCE))
                .build();
    }
}