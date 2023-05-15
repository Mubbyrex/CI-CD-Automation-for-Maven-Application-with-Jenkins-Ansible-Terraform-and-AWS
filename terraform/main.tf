# remote state
# terraform {
#   required_version = ">= 0.12"
#   backend "s3" {
#     bucket = "aws-bucket-name"
#     key    = "terraform.tfstate"
#   }
# }

provider "aws" {
  region = "us-east-1"
}

# data for amazon linux machine
data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023.0.20230503.0-kernel-6.1-x86_64"]
  }
}

# aws ec2 instance resource
resource "aws_instance" "ec2_instance" {
  ami           = data.aws_ami.amazon_linux.id
  instance_type = "t2.micro"
  key_name      = "Terraform_hands_on"
  monitoring    = true
  vpc_security_group_ids = [aws_security_group.myapp-sg.id]
  subnet_id              = module.myapp-vpc.public_subnets[0]
  associate_public_ip_address = true
  availability_zone = var.availability_zone

  tags = {
    Terraform   = "true"
    Environment = "dev"
  }

  provisioner "local-exec" {
    working_dir = "/workspaces/Jenkins-Project/ansible"
    command = "ansible-playbook --inventory ${self.public_ip}, --private-key ${var.private_key} --user ec2-user docker-playbook.yaml"
  }
}

# module "ec2_instance" {
#   source  = "terraform-aws-modules/ec2-instance/aws"

#   name = "single-instance"

#   instance_type          = "t2.micro"
#   key_name               = "Terraform_hands_on"
#   monitoring             = true
#   vpc_security_group_ids = [aws_security_group.myapp-sg.id]
#   subnet_id              = module.myapp-vpc.public_subnets[0]
#   associate_public_ip_address = true
#   availability_zone = var.availability_zone
#   ami = data.aws_ami.amazon_linux.id

#   tags = {
#     Terraform   = "true"
#     Environment = "dev"
#   }

#   provisioner "local-exec" {
#     working_dir = "/workspaces/Jenkins-Project/ansible"
#     command = "ansible-playbook -i ${self.public_ip}, --private-key ${var.private_key} --user ec2-user playbook.yml"
#   }
# }

data "aws_availability_zones" "available" {}

module "myapp-vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "4.0.1"

  name = "myapp-vpc"
  cidr = var.vpc_cidr_block
  private_subnets = var.private_subnet_cidr_blocks
  public_subnets = var.public_subnet_cidr_blocks
  azs = data.aws_availability_zones.available.names 
  
  enable_nat_gateway = false
  # allows all subnet to access internet through a single NAT Gateway
  single_nat_gateway = true
  enable_dns_hostnames = true

}


resource "aws_security_group" "myapp-sg" {
  name   = "myapp-sg"
  vpc_id = module.myapp-vpc.vpc_id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    cidr_blocks     = ["0.0.0.0/0"]
    prefix_list_ids = []
  }
}


output "ec2_public_ip" {
  value = aws_instance.ec2_instance.public_ip
}