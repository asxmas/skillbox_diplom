package searchapp.dao;

import searchapp.entity.Field;

import java.util.List;

public interface FieldDAO {
    Field findFieldById(int id);

    void saveField(Field field);

    void updateField(Field field);

    void deleteField(Field field);

    List<Field> findAllFields();
}
