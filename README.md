# Hungry Orca

#### learning project upload & download files
#### with **Spring-Boot & MySQL**
___

### Backend:
+ GET REQUEST (***Retrieve*** all files)
> http://localhost:8080/api/orca
+ GET REQUEST (***Download*** a file)
> http://localhost:8080/api/orca/download?fileId
+ POST REQUEST (***Upload*** a file)
> http://localhost:8080/api/orca/upload
> ~payload: Multipartfile