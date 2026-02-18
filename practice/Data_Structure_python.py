####
## LIST
###
courses = ['History', 'Math','Physics', 'Compsics' ]

print(courses)
print(len(courses))
print(courses[1])
print(courses[-1])
print(courses[2:])
print(courses[0:2])

courses.append('Art')

print(courses)

courses.insert(0,'English')
print(courses)

courses_2 = ['abc','xyz']
# courses.insert(0, courses_2)
# print(courses)

courses.extend(courses_2)
print(courses)

courses.remove('Math')
print(courses)
popped = courses.pop()
print(courses)
# print(popped)
# courses.reverse()
courses.sort()
num = [1,5,2,8,3]
num.sort()
print(num)
num.sort(reverse=True)
print(num)

print(courses)

print(courses.index('English'))

for index, i in enumerate(courses):
    print (index, i)

course_str =','.join(courses)
print(course_str)

#### Tuples

tuple_1 = ('History', 'Math','Physics', 'Compsics', 'Math' )
tuple_2 = tuple_1
print(tuple_1)
print(tuple_2)
# tuple_1[0] = 'Art'
# print(tuple_1)
# print(tuple_2)


###### SETS


set = {'History', 'Math','Physics', 'Compsics'}

set_duplicate = {'History', 'Math','Physics', 'Compsics', 'History'}
art_duplicate = {'History', 'Math','Art', 'Compsics'}

print(set)
# print(set_duplicate)
print(set.intersection(art_duplicate))

print(set.difference(art_duplicate))

print(set.union(art_duplicate))



#### Dictionaries


student = {'name': 'John', 'age':25, 'courses':['Math','CompSci']}
student['phone'] = '555-555'
print(student['courses'])
print(student.get('name'))
print(student.get('phone','Not Found'))
student.update({'name': 'Jane', 'age':20, 'courses':['History','Art'], 'phone': '111-111'})
print (student)
del student['age']
print(student)
student.update({'name': 'Jane', 'age':27, 'courses':['History','Art'], 'phone': '111-111'})
age = student.pop('age')
print(age)
print(len(student))
print(student.keys())
print(student.values())
print(student.items())


for key, value in student.items():
    print(key, value)

nums = [1,2,3,4,5,6,7,8,9,10]
my_list = [n for n in nums if n % 2 == 0]
print(my_list)



