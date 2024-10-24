Demo App

Configure the endpoints and actions dynamically in the app.
    
```yaml
coral:
  endpoints:
    - name: health
      url: myhealth
      actions:
        - "request|http://localhost:8080/health"
        - "wait_random|1000"
    - name: info
      url: myinfo
      actions:
        - "request|http://localhost:8080/health"
        - "wait_random|1000"
```