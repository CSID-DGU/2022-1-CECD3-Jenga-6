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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
}
