#locals {
##  init_script = "#!/usr/bin/env \nls -al\napt-get update"
#  init_script = "#!/usr/bin/env"
#}

#resource "ncloud_init_script" "init" {
#  content = local.init_script
#  name = "sc-${local.project_name}"
#}
