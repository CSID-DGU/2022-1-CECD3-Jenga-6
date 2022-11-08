package com.jenga.weather.web.infravisualization.service;

import com.jenga.weather.domain.base.AWSEntity;
import com.jenga.weather.domain.ec2.model.EC2;
import com.jenga.weather.domain.ec2.service.EC2Service;
import com.jenga.weather.domain.elb.model.ELB;
import com.jenga.weather.domain.elb.service.ELBService;
import com.jenga.weather.domain.igw.model.IGW;
import com.jenga.weather.domain.igw.service.IGWService;
import com.jenga.weather.domain.nat.model.NAT;
import com.jenga.weather.domain.nat.service.NATService;
import com.jenga.weather.domain.rds.model.RDS;
import com.jenga.weather.domain.rds.service.RDSService;
import com.jenga.weather.domain.s3.model.S3;
import com.jenga.weather.domain.s3.service.S3Service;
import com.jenga.weather.domain.subnet.model.Subnet;
import com.jenga.weather.domain.subnet.service.SubnetService;
import com.jenga.weather.domain.vpc.model.VPC;
import com.jenga.weather.domain.vpc.service.VPCService;
import com.jenga.weather.web.infravisualization.node.Graph;
import com.jenga.weather.web.infravisualization.node.LeafNode;
import com.jenga.weather.web.infravisualization.node.RootNode;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VisualizationService {

    private final EC2Service ec2Service;
    private final ELBService elbService;
    private final IGWService igwService;
    private final NATService natService;
    private final RDSService rdsService;
    private final S3Service s3Service;
    private final SubnetService subnetService;
    private final VPCService vpcService;

    public String mapInfra() {
        Ec2Client ec2Client = Ec2Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // Infra Resource 목록 조회
        List<EC2> ec2List = ec2Service.getEC2Instances(ec2Client);
        List<ELB> elbList = elbService.getELBInstances();
        List<IGW> igwList = igwService.getIGWInstances(ec2Client);
        List<NAT> natList = natService.getNATInstances(ec2Client);
        List<RDS> rdsList = rdsService.getRDSInstances();
        List<S3> s3List = s3Service.getS3Instances();
        List<Subnet> subnetList = subnetService.getSubnetInstances(ec2Client);
        List<VPC> vpcList = vpcService.getVPCInstances(ec2Client);

        // Subnet 별 매핑
        HashMap<Subnet, List<AWSEntity>> subnetMapping = new HashMap<>();
        subnetList.forEach(subnet -> {
            List<AWSEntity> entities = new ArrayList<>();
            ec2List.forEach(vv -> {
                if (vv.getSubnetId().equals(subnet.getResourceId()))
                    entities.add(vv);
            });
            natList.forEach(vv -> {
                if (vv.getSubnetId().equals(subnet.getResourceId()))
                    entities.add(vv);
            });
            rdsList.forEach(vv -> {
                if (vv.getSubnetId().equals(subnet.getResourceId()))
                    entities.add(vv);
            });
            subnetMapping.put(subnet, entities);
        });

        // VPC 별 매핑
        HashMap<VPC, List<Subnet>> vpcMapping = new HashMap<>();
        vpcList.forEach(v -> {
            List<Subnet> subnets = new ArrayList<>();
            subnetMapping.forEach((k, vv) -> {
                if (v.getResourceId().equals(k.getVpcId())) {
                    subnets.add(k);
                }
                vpcMapping.put(v, subnets);
            });
        });

        // Infra 관계 생성
        StringBuffer result = new StringBuffer();
        result.append("VPC-Subnet 별 Resource\n");
        result.append("--------------------------------------------------\n");
        vpcMapping.forEach((vpc, subnets) -> {
            result.append("VPC : ").append(vpc).append("\n");
            subnets.forEach(subnet -> {
                result.append("    |Subnet : ").append(subnet).append("\n");
                subnetMapping.get(subnet).forEach(entity ->
                        result.append("        |").append(entity.toString()).append("\n")
                );
                elbList.forEach(entity -> {
                    if (entity.getVpcId().equals(vpc.getResourceId()))
                        result.append("    |").append(entity).append("\n");
                });
                igwList.forEach(entity -> {
                    if (entity.getVpcId().equals(vpc.getResourceId()))
                        result.append("        |").append(entity).append("\n");
                });
                result.append("\n");
            });
            result.append("\n");
            System.out.println();
        });
        result.append("S3-------------------------------------------------\n");
        s3List.forEach(entity ->
                result.append(entity.toString()).append("\n")
        );

        return result.toString();
    }

    public String getData() {
        int subnetIdx = 0;
        int ec2Idx = 0;
        int natIdx = 0;

        Graph graph = new Graph();
        RootNode rootNode = new RootNode();

        Ec2Client ec2Client = Ec2Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // Infra Resource 목록 조회
        List<EC2> ec2List = ec2Service.getEC2Instances(ec2Client);
        List<ELB> elbList = elbService.getELBInstances();
        List<IGW> igwList = igwService.getIGWInstances(ec2Client);
        List<NAT> natList = natService.getNATInstances(ec2Client);
        List<Subnet> subnetList = subnetService.getSubnetInstances(ec2Client);


        List<JSONObject> rdsList = new ArrayList<>();
        int rds_idx = 0;
        for (RDS rds : rdsService.getRDSInstances()) {
            JSONObject rds_node = new JSONObject();
            rds_node.put("id", rds.getResourceId() + '@' + rds_idx);
            rds_node.put("name", rds.getResourceName());
            rds_node.put("href", "/img/aws_rds.png");
            rds_node.put("engine", rds.getEngine());
            rds_node.put("vpc_id", rds.getVpcId());
            rds_node.put("subnet_id", rds.getSubnetId());
            rds_node.put("status", rds.getDbInstanceStatus());
            rds_node.put("dbInstanceClass", rds.getDbInstanceClass());
            rds_node.put("region", rds.getRegion());
            rds_node.put("children", new JSONArray().put(new LeafNode()));
            rdsList.add(rds_node);
            rds_idx += 1;
        }

        for (S3 s3 : s3Service.getS3Instances()) {
            JSONObject s3_node = new JSONObject();
            s3_node.put("id", "s3-" + s3.getResourceName());
            s3_node.put("name", s3.getResourceName());
            s3_node.put("href", "/img/aws_s3.png");
            s3_node.put("children", new JSONArray().put(new LeafNode()));
            rootNode.addChildren(s3_node);
        }

        int vpcIdx = 0;
        int igwIdx = 0;
        for (VPC vpc : vpcService.getVPCInstances(ec2Client)) {
            JSONObject vpc_node = new JSONObject();
            vpc_node.put("id", vpc.getResourceId());
            vpc_node.put("name", "vpc-" + vpcIdx);
            vpc_node.put("resource_name", vpc.getResourceName());
            vpc_node.put("size", 5);
            vpc_node.put("href", "/img/aws_vpc.png");
            vpc_node.put("children", new JSONArray().put(new LeafNode()));

            List<IGW> igws = igwList.stream()
                    .filter(i -> i.getVpcId().equals(vpc.getResourceId()))
                    .collect(Collectors.toList());

            IGW igw = igws.get(0);
            JSONObject igw_node = new JSONObject();
            igw_node.put("id", igw.getResourceId());
            igw_node.put("name", "igw-" + vpcIdx + "-" + igwIdx);
            igw_node.put("owner_id", igw.getOwnerId());
            igw_node.put("size", 4);
            igw_node.put("href", "/img/aws_igw.png");
            igw_node.put("children", new JSONArray().put(new LeafNode()));
            vpc_node.getJSONArray("children").put(igw_node);

            List<ELB> elbs = elbList.stream()
                    .filter(e -> e.getVpcId().equals(vpc.getResourceId()))
                    .collect(Collectors.toList());

            for (ELB elb : elbs) {
                JSONObject elb_node = new JSONObject();
                elb_node.put("id", "elb-" + vpc_node.get("id"));
                elb_node.put("name", "elb-" + elb.getResourceName());
                elb_node.put("href", "/img/aws_elb.png");
                elb_node.put("state", elb.getState());
                elb_node.put("vpc_id", elb.getVpcId());
                elb_node.put("resource_name", elb.getResourceName());
                elb_node.put("children", new JSONArray().put(new LeafNode()));
                graph.addLink(new JSONObject()
                        .put("source", elb_node.get("id"))
                        .put("target", igw_node.get("id")));
                vpc_node.getJSONArray("children").put(elb_node);
            }

            List<Subnet> subnets = subnetList.stream()
                    .filter(s -> s.getVpcId().equals(vpc.getResourceId()))
                    .collect(Collectors.toList());

            for (Subnet subnet : subnets) {
                JSONObject subnet_node = new JSONObject();
                subnet_node.put("id", subnet.getResourceId());
                subnet_node.put("name", "subnet-" + vpcIdx + "-" + subnetIdx);
                subnet_node.put("size", 4);
                subnet_node.put("resource_name", subnet.getResourceName());
                subnet_node.put("vpc_id", subnet.getVpcId());
                subnet_node.put("children", new JSONArray().put(new LeafNode()));

                List<NAT> nats = natList.stream()
                        .filter(n -> n.getVpcId().equals(vpc.getResourceId())
                                && n.getSubnetId().equals(subnet.getResourceId()))
                        .collect(Collectors.toList());

                for (NAT nat : nats) {
                    JSONObject nat_node = new JSONObject();
                    nat_node.put("id", nat.getResourceId());
                    nat_node.put("name", "nat-" + vpcIdx + "-" + subnetIdx + "-" + natIdx);
                    nat_node.put("href", "/img/aws_nat.png");
                    nat_node.put("private_ip", nat.getPrivateIP());
                    nat_node.put("public_ip", nat.getPublicIP());
                    nat_node.put("vpc_id", nat.getVpcId());
                    nat_node.put("subnet_id", nat.getSubnetId());
                    nat_node.put("children", new JSONArray().put(new LeafNode()));
                    subnet_node.getJSONArray("children").put(nat_node);
                    natIdx += 1;
                }
                natIdx = 0;

                List<JSONObject> rdsNodes = rdsList.stream()
                        .filter(r -> r.get("vpc_id").equals(vpc.getResourceId())
                                && r.get("subnet_id").equals(subnet.getResourceId()))
                        .collect(Collectors.toList());

                for (JSONObject rdsNode : rdsNodes) {
                    rdsNode.put("name", "rds-" + vpcIdx + "-" + subnetIdx);
                    subnet_node.getJSONArray("children").put(rdsNode);
                }

                List<EC2> ec2s = ec2List.stream()
                        .filter(e -> e.getSubnetId().equals(subnet.getResourceId()))
                        .collect(Collectors.toList());

                for (EC2 ec2 : ec2s) {
                    JSONObject ec2_node = new JSONObject();
                    ec2_node.put("id", ec2.getResourceId());
                    ec2_node.put("name", "ec2-" + vpcIdx + "-" + subnetIdx + "-" + ec2Idx);
                    ec2_node.put("size", 3);
                    ec2_node.put("href", "/img/aws_ec2.png");
                    ec2_node.put("type", ec2.getInstanceType());
                    ec2_node.put("private_dns_name", ec2.getPrivateDnsName());
                    ec2_node.put("private_ip_address", ec2.getPrivateIpAddress());
                    ec2_node.put("image_id", ec2.getImageId());
                    ec2_node.put("key_name", ec2.getKeyName());
                    ec2_node.put("subnet_id", ec2.getSubnetId());
                    ec2_node.put("vpc_id", ec2.getVpcId());
                    ec2_node.put("resource_name", ec2.getResourceName());
                    ec2_node.put("children", new JSONArray().put(new LeafNode()));
                    subnet_node.getJSONArray("children").put(ec2_node);
                    ec2Idx += 1;
                }
                ec2Idx = 0;
                vpc_node.getJSONArray("children").put(subnet_node);
                subnetIdx += 1;
            }
            subnetIdx = 0;
            rootNode.addChildren(vpc_node);
            vpcIdx += 1;
        }

        graph.setRoot(rootNode);

        return graph.toString();
    }
}
