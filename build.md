### Build and push to DockerHub
```Bash
docker login
```
```Bash
docker build -f Dockerfile -t dnadas98/priv:freeroam_summits . && \
docker push dnadas98/priv:freeroam_summits
```