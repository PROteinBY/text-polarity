# text-polarity
Haw to start:
```
mvn clean install
mvn jetty:run
```


## Knn

1. Dataset: [click](http://ai.stanford.edu/~amaas/data/sentiment/)
2. Приводим к нижнему ригистру, чистим от знаков припенания и прочего "мусора"
3. Собираем частоты слов для позитивных и негативных отзывов (отдельно)
4. Строим ВСЕ возможные n-граммы (n = 2, грамма = слово)
5. Собираем частоты всех n-грамм для всех отзывов
6. Для каждой n-граммы строим вероятность появления второго слова после первого (количество n-грамм / частота первого слова из n-граммы)
   Частоты слов для позитивных и негативных отзывов собирались отдельно, что бы усилить веса негативных или позитивных n-грамм
    ```
    bad film - в негативных отзывах теперь имеет вес в ~2 раза выше, чем в позитивных
    excellent film - в позитивных имеет вес в ~2 раза больше, чем в негативных
    ```
7. Теперь для каждого отзыва нужно вырезать только встречающиеся в нём n-граммы и сохранить все n-граммы отзывов для дальнейшего использования
8. Теперь подается какой то текст. Выбиратся все n-граммы (с веслом 1 по умолчанию). Далее нужно сравнить эти n-граммы с n-граммами всех отзывов
9. Показатель "совпадения" расчитывается как скалярное произведение двух векторов (которые содержат пересечение n-грамм между входным отзывом и рравниваемом, но разные веса (1 для входного))
10. Полученное произведение - показатель приближенности отзыва к отзыву из обучающей выборки.
11. Получим такие показатели, отсортируем их по убыванию и выберем K первых.
12. Ответом будет оценка большинства отзывов из K отобранных.

Класс, реализующий сравнения: [KNeighborPolarityDetector](https://github.com/PROteinBY/text-polarity/blob/master/src/main/java/by/bstu/feis/ii12/core/KNeighborPolarityDetector.java)<br/>
Получение файла с n-граммами и весами для отзывов: [click](https://github.com/PROteinBY/text-polarity/blob/master/src/test/java/by/bstu/feis/ii12/core/KNeighborPolarityDetectorTest.java)